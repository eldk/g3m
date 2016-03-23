//
//  PyramidElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Sebastian Ortega Trujillo on 4/3/16.
//
//

#include "PyramidElevationDataProvider.hpp"
#include "ShortBufferElevationData.hpp"
#include "IStringBuilder.hpp"
#include "DownloadPriority.hpp"
#include "G3MContext.hpp"
#include "TimeInterval.hpp"


#include <sstream>

class PyramidParser : public GAsyncTask {
private:
    IByteBuffer *_buffer;
    const Sector *_sector;
    IElevationDataListener *_listener;
    bool _autodeleteListener;
    int _noDataValue;
    double _deltaHeight;
    MutableVector2I &_minRes;
public:
    
    PyramidParser (IByteBuffer *buffer, const Sector *sector,
                   IElevationDataListener *listener, bool autodeleteListener,
                   int noDataValue, double deltaHeight,
                   MutableVector2I &minRes) :
    _buffer(buffer),
    _sector(sector),
    _listener(listener),
    _autodeleteListener(autodeleteListener),
    _noDataValue(noDataValue),
    _deltaHeight(deltaHeight),
    _minRes(minRes){
        
    }
    
    void runInBackground(const G3MContext* context) {
        const Vector2I *resolution = JSONDemParser::getResolution(_buffer);
        ShortBufferElevationData *elevationData = JSONDemParser::parseJSONDemElevationData(*_sector, *resolution, _buffer,(short) _noDataValue, _deltaHeight);
        
        if (_buffer != NULL){
            delete _buffer;
        }
        
        if (elevationData == NULL)
        {
            _listener->onError(*_sector, *resolution);
        }
        else
        {
            _listener->onData(*_sector, *resolution, elevationData);
            if ((_minRes.x() * _minRes.y()) > (resolution->_x * resolution->_y)){
                _minRes = resolution->asMutableVector2I();
            }
        }
        
        
        if (_autodeleteListener)
        {
            if (_listener != NULL){
                delete _listener;
            }
            _listener = NULL;
        }
#ifdef C_CODE
        delete resolution;
#endif

    }
    
    void onPostExecute(const G3MContext* context) {
       
    }
    
    ~PyramidParser(){
        delete _sector;
        
#ifdef JAVA_CODE
        super.dispose();
#endif
    }
};


class PyramidElevationDataProvider_BufferDownloadListener : public IBufferDownloadListener {
private:
    
    const Sector *_sector;
    int _width, _height;
    MutableVector2I &_minRes;
    IElevationDataListener *_listener;
    bool _autodeleteListener;
    double _deltaHeight;
    int _noDataValue;
    const IThreadUtils *_threadUtils;
    
public:
    PyramidElevationDataProvider_BufferDownloadListener(const Sector* sector,
                                                        const Vector2I& extent,
                                                        IElevationDataListener *listener,
                                                        bool autodeleteListener,
                                                        int noDataValue,
                                                        double deltaHeight,
                                                        MutableVector2I &minRes,
                                                        const IThreadUtils *threadUtils):
    _sector(sector),
    _width(extent._x),
    _height(extent._y),
    _listener(listener),
    _minRes(minRes),
    _autodeleteListener(autodeleteListener),
    _deltaHeight(deltaHeight),
    _noDataValue(noDataValue),
    _threadUtils(threadUtils){
        
    }
    
    void onDownload(const URL& url,
                    IByteBuffer* buffer,
                    bool expired){
        
        _threadUtils->invokeAsyncTask (new PyramidParser(buffer, _sector, _listener, _autodeleteListener,_noDataValue,_deltaHeight,_minRes),true);
    }
    
    void onError(const URL& url){
        const Vector2I resolution = Vector2I(_width, _height);
        
        _listener->onError(*_sector, resolution);
        if (_autodeleteListener)
        {
            if (_listener != NULL){
              delete _listener;
            }
            _listener = NULL;
        }
    }

    void onCancel(const URL& url){
        if (_listener != NULL)
        {
            const Vector2I resolution = Vector2I(_width, _height);
            _listener->onCancel(*_sector, resolution);
            if (_autodeleteListener)
            {
                if (_listener != NULL) {
                  delete _listener;
                }
                _listener = NULL;
            }
        }
    }

    
    void onCanceledDownload(const URL& url,
                            IByteBuffer* data,
                            bool expired){
        if (_autodeleteListener)
        {
            if (_listener != NULL){
              delete _listener;
            }
            _listener = NULL;
        }
    }
    
};




PyramidElevationDataProvider::PyramidElevationDataProvider(const std::string &layer, const Sector& sector,
                                                           int noDataValue,
                                                           double deltaHeight): _sector(sector), _layer(layer), _noDataValue(noDataValue){
  _pyrComposition = new std::vector<PyramidComposition>();
  _deltaHeight = deltaHeight;
  _minRes = MutableVector2I(256, 256);
}

PyramidElevationDataProvider::~PyramidElevationDataProvider(){
  _pyrComposition->clear();
  delete _pyrComposition;
  _pyrComposition = NULL;
    
#ifdef JAVA_CODE
    super.dispose();
#endif
  
}

void PyramidElevationDataProvider::getMetadata() const{
  
  _downloader->requestBuffer(URL(requestMetadataPath(),false), DownloadPriority::HIGHER, TimeInterval::fromDays(30), true, new MetadataListener(_pyrComposition), true);
}

void PyramidElevationDataProvider::initialize(const G3MContext* context ){
  _downloader = context->getDownloader();
  _threadUtils = context->getThreadUtils();
  getMetadata();
}

const long long PyramidElevationDataProvider::requestElevationData(const Sector& sector,
                                             const Vector2I& extent,
                                             const Tile * tile,
                                             IElevationDataListener* listener,
                                             bool autodeleteListener){
    Sector * sectorCopy = new Sector(sector);
    const int level = tile->_level;
    const int row = tile->_row;
    const int column = tile->_column;

    if ((_downloader == NULL) || (aboveLevel(*sectorCopy, level))){
        delete sectorCopy;
        return -1;
    }
    
    
    std::string path = requestStringPath(_layer,level,row,column);
    
    return _downloader->requestBuffer(URL(path,false), DownloadPriority::HIGHEST - level, TimeInterval::fromDays(30), true, new PyramidElevationDataProvider_BufferDownloadListener(sectorCopy, extent, listener, autodeleteListener,_noDataValue, _deltaHeight, _minRes, _threadUtils), true );
    
}

std::string PyramidElevationDataProvider::requestStringPath(const std::string & layer, int level, int row, int column){
  
    IStringBuilder *istr = IStringBuilder::newStringBuilder();
    istr->addString(_layer);
    istr->addInt(level);
    istr->addString("/");
    istr->addInt(column);
    istr->addString("/");
    istr->addInt(row);
    istr->addString(".json");
    std::string res = istr->getString();
    delete istr;
    return res;
}

std::string PyramidElevationDataProvider::requestMetadataPath() const{
  return _layer + "meta.json";
}

void PyramidElevationDataProvider::cancelRequest(const long long requestId){
  _downloader->cancelRequest(requestId);
}

std::vector<const Sector*> PyramidElevationDataProvider::getSectors() const{
  std::vector<const Sector*> sectors;
  sectors.push_back(&_sector);
  return sectors;
}

bool PyramidElevationDataProvider::aboveLevel(const Sector &sector, int level){
  int maxLevel = 0;
  for (size_t i=0; i< _pyrComposition->size(); i++) {
    if (sector.touchesWith(_pyrComposition->at(i).getSector())) {
      maxLevel = IMathUtils::instance()->max(maxLevel,_pyrComposition->at(i)._pyramidLevel);
    }
  }

  return ((level > maxLevel) || (!sector.touchesWith(_sector)));
}

const Vector2I PyramidElevationDataProvider::getMinResolution() const {
    return _minRes.asVector2I();
}



