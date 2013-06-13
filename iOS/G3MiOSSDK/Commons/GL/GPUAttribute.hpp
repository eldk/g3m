//
//  Attribute.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//

#ifndef G3MiOSSDK_Attribute_h
#define G3MiOSSDK_Attribute_h


#include "INativeGL.hpp"
#include "IFloatBuffer.hpp"
#include "GL.hpp"

#include "IStringBuilder.hpp"

class GPUAttribute;

class GPUAttributeValue{
protected:
  const bool _enabled;
  const int _type;
  const int _attributeSize;
  const int _index;
  int           _stride;
  bool          _normalized;
  
  const int _arrayElementSize;
  
  mutable GPUAttribute* _attribute;
  
public:
  
  GPUAttributeValue(bool enabled):
  _enabled(enabled),
  _type(0),
  _attributeSize(0),
  _index(0),
  _stride(0),
  _normalized(0),
  _arrayElementSize(0),
  _attribute(NULL){}
  
  GPUAttributeValue(int type, int attributeSize, int arrayElementSize, int index, int stride, bool normalized):
  _enabled(true),
  _type(type),
  _attributeSize(attributeSize),
  _index(index),
  _stride(stride),
  _normalized(normalized),
  _arrayElementSize(arrayElementSize),
  _attribute(NULL){}
  
  int getType() const { return _type;}
  int getAttributeSize() const { return _attributeSize;}
  int getIndex() const { return _index;}
  int getStride() const { return _stride;}
  bool getNormalized() const { return _normalized;}
  bool getEnabled() const { return _enabled;}
  GPUAttribute* getLinkedAttribute() const { return _attribute;}
  virtual ~GPUAttributeValue(){}
  virtual void setAttribute(GL* gl, const int id) const = 0;
  virtual bool isEqualsTo(const GPUAttributeValue* v) const = 0;
  virtual GPUAttributeValue* shallowCopy() const = 0;
  
  virtual std::string description() const = 0;
  
  void linkToGPUAttribute(GPUAttribute* a){
    _attribute = a;
  }
  
  void unLinkToGPUAttribute(){
    _attribute = NULL;
  }
  
  void setValueToLinkedAttribute() const;
  
  
};

class GPUAttribute{
protected:
  const std::string _name;
  const int _id;
  
  bool _dirty;
  GPUAttributeValue* _value;
  
  const int _type;
  const int _size;
  
  
  bool _dirtyEnabled;
  bool _enabled;
  
public:
  
  virtual ~GPUAttribute(){
    delete _value;
  }
  
  GPUAttribute(const std::string&name, int id, int type, int size):
  _name(name),
  _id(id),
  _dirty(false),
  _value(NULL),
  _type(type),
  _size(size),
  _enabled(false),
  _dirtyEnabled(false){}
  
  const std::string getName() const{ return _name;}
  const int getID() const{ return _id;}
  int getType() const{ return _type;}
  int getSize() const{ return _size;}
  bool wasSet() const{ return _value != NULL;}
  bool isEnabled() const { return _enabled;}
  
  void unset(GL* gl){
    if (_value != NULL){
      delete _value;
      _value = NULL;
    }
    _enabled = false;
    _dirty = false;
    _dirtyEnabled = false;
    
    gl->disableVertexAttribArray(_id);
  }
  
  void set(GPUAttributeValue* v){
    if (v->getEnabled() && _type != v->getType()){ //type checking
      //delete v;
      ILogger::instance()->logError("Attempting to set attribute " + _name + "with invalid value type.");
      return;
    }
    if (_value == NULL || !_value->isEqualsTo(v)){
      _dirty = true;
      if (_value != NULL){
        delete _value;
      }
      _value = v->shallowCopy();
      //delete v;
    }
  }
  
  //  void setEnable(bool b){
  //    if (b != _enabled){
  //      _enabled = b;
  //      _dirtyEnabled = true;
  //    }
  //  }
  
  virtual void applyChanges(GL* gl){
    //    if (_dirtyEnabled){
    //      _dirtyEnabled = false;
    //      if (_enabled){
    //        gl->enableVertexAttribArray(_id);
    //      } else{
    //        gl->disableVertexAttribArray(_id);
    //      }
    //    }
    
    if (_dirty){
      
      if (_value->getEnabled()){
        if (!_enabled){
          gl->enableVertexAttribArray(_id);
          _enabled = true;
        }
        _value->setAttribute(gl, _id);
      } else{
        if (_enabled){
          gl->disableVertexAttribArray(_id);
          _enabled = false;
        }
      }
      
      _dirty = false;
    }
  }
};

///////////

class GPUAttributeValueDisabled: public GPUAttributeValue{
public:
  GPUAttributeValueDisabled():
  GPUAttributeValue(false){}
  
  void setAttribute(GL* gl, const int id) const{
  }
  
  bool isEqualsTo(const GPUAttributeValue* v) const{
    return (v->getEnabled() == false);
  }
  
  GPUAttributeValue* shallowCopy() const{
    return new GPUAttributeValueDisabled();
  }
  
  std::string description() const{
    return "Attribute Disabled.";
  }
  
};

class GPUAttributeValueVecFloat: public GPUAttributeValue{
  IFloatBuffer* _buffer;
  int _timeStamp;
public:
  GPUAttributeValueVecFloat(IFloatBuffer* buffer, int attributeSize, int arrayElementSize, int index, int stride, bool normalized):
  GPUAttributeValue(GLType::glFloat(), attributeSize, arrayElementSize, index, stride, normalized),
  _buffer(buffer),
  _timeStamp(buffer->timestamp()){}
  
  void setAttribute(GL* gl, const int id) const{
    if (_index != 0){
      //TODO: Change vertexAttribPointer
      ILogger::instance()->logError("INDEX NO 0");
    }
    
    gl->vertexAttribPointer(id, _arrayElementSize, _normalized, _stride, _buffer);
  }
  
  bool isEqualsTo(const GPUAttributeValue* v) const{
    
    if (!v->getEnabled()){
      return false;          //Is a disabled value
    } else{
      GPUAttributeValueVecFloat* vecV = (GPUAttributeValueVecFloat*)v;
      return (_buffer == vecV->_buffer) &&
              (_timeStamp == vecV->_timeStamp) &&
               (_type == v->getType()) &&
               (_attributeSize == v->getAttributeSize()) &&
               (_stride == v->getStride()) &&
               (_normalized == v->getNormalized() ) ;
    }
  }
  
  GPUAttributeValue* shallowCopy() const{
    GPUAttributeValueVecFloat* v = new GPUAttributeValueVecFloat(_buffer, _attributeSize,
                                                                 _arrayElementSize,
                                                                 _index,
                                                                 _stride,
                                                                 _normalized);
    v->_timeStamp = _timeStamp;
    return v;
  }
  
  std::string description() const{
    
    IStringBuilder *isb = IStringBuilder::newStringBuilder();
    isb->addString("Attribute Value Float.");
    isb->addString(" ArrayElementSize:");
    isb->addInt(_arrayElementSize);
    isb->addString(" AttributeSize:");
    isb->addInt(_attributeSize);
    isb->addString(" Index:");
    isb->addInt(_index);
    isb->addString(" Stride:");
    isb->addInt(_stride);
    isb->addString(" Normalized:");
    isb->addBool(_normalized);
    
    std::string s = isb->getString();
    delete isb;
    return s;
  }
  
};

class GPUAttributeValueVec1Float: public GPUAttributeValueVecFloat{
public:
  GPUAttributeValueVec1Float(IFloatBuffer* buffer, int arrayElementSize, int index, int stride, bool normalized):
  GPUAttributeValueVecFloat(buffer, 1, arrayElementSize, index, stride, normalized){}
};
class GPUAttributeVec1Float: public GPUAttribute{
public:
  GPUAttributeVec1Float(const std::string&name, int id):GPUAttribute(name, id, GLType::glFloat(), 1){}
};
////////
///////////
class GPUAttributeValueVec2Float: public GPUAttributeValueVecFloat{
public:
  GPUAttributeValueVec2Float(IFloatBuffer* buffer, int arrayElementSize, int index, int stride, bool normalized):
  GPUAttributeValueVecFloat(buffer, 2, arrayElementSize, index, stride, normalized){}
};

class GPUAttributeVec2Float: public GPUAttribute{
public:
  GPUAttributeVec2Float(const std::string&name, int id):GPUAttribute(name, id, GLType::glFloat(), 2){}
};
////////

///////////
class GPUAttributeValueVec3Float: public GPUAttributeValueVecFloat{
public:
  GPUAttributeValueVec3Float(IFloatBuffer* buffer, int arrayElementSize, int index, int stride, bool normalized):
  GPUAttributeValueVecFloat(buffer, 3, arrayElementSize, index, stride, normalized){}
};
class GPUAttributeVec3Float: public GPUAttribute{
public:
  GPUAttributeVec3Float(const std::string&name, int id):GPUAttribute(name, id, GLType::glFloat(), 3){}
};
////////

///////////
class GPUAttributeValueVec4Float: public GPUAttributeValueVecFloat{
public:
  GPUAttributeValueVec4Float(IFloatBuffer* buffer, int arrayElementSize, int index, int stride, bool normalized):
  GPUAttributeValueVecFloat(buffer, 4, arrayElementSize, index, stride, normalized){}
};
class GPUAttributeVec4Float: public GPUAttribute{
public:
  GPUAttributeVec4Float(const std::string&name, int id):GPUAttribute(name, id, GLType::glFloat(), 4){}
};
////////

#endif
