package org.glob3.mobile.generated; 
public class ShapesRenderer extends LeafRenderer
{
  private static class LoadQueueItem
  {
    public final URL _url;
    public final TimeInterval _timeToCache;
    public final long _priority;
    public final boolean _readExpired;
    public final String _uriPrefix;
    public final boolean _isTransparent;
    public Geodetic3D _position;
    public final AltitudeMode _altitudeMode;
    public ShapeLoadListener _listener;
    public final boolean _deleteListener;
    public final boolean _isBSON;

    public LoadQueueItem(URL url, long priority, TimeInterval timeToCache, boolean readExpired, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener, boolean deleteListener, boolean isBSON)
    {
       _url = url;
       _priority = priority;
       _timeToCache = timeToCache;
       _readExpired = readExpired;
       _uriPrefix = uriPrefix;
       _isTransparent = isTransparent;
       _position = position;
       _altitudeMode = altitudeMode;
       _listener = listener;
       _deleteListener = deleteListener;
       _isBSON = isBSON;

    }

    public void dispose()
    {
    }
  }


  private final boolean _renderNotReadyShapes;

  private java.util.ArrayList<Shape> _shapes = new java.util.ArrayList<Shape>();

  private ShapeTouchListener _shapeTouchListener;
  private boolean _autoDeleteShapeTouchListener;


  private G3MContext _context;
  private Camera    _lastCamera;

  private GLState _glState;
  private GLState _glStateTransparent;

  private void updateGLState(G3MRenderContext rc)
  {
  
    final Camera cam = rc.getCurrentCamera();
    /*
  
     if (_projection == NULL) {
     _projection = new ProjectionGLFeature(cam->getProjectionMatrix44D());
     _glState->addGLFeature(_projection, true);
     _glStateTransparent->addGLFeature(_projection, true);
     } else{
     _projection->setMatrix(cam->getProjectionMatrix44D());
     }
  
     if (_model == NULL) {
     _model = new ModelGLFeature(cam->getModelMatrix44D());
     _glState->addGLFeature(_model, true);
     _glStateTransparent->addGLFeature(_model, true);
     } else{
     _model->setMatrix(cam->getModelMatrix44D());
     }
     */
    ModelViewGLFeature f = (ModelViewGLFeature) _glState.getGLFeature(GLFeatureID.GLF_MODEL_VIEW);
    if (f == null)
    {
      _glState.addGLFeature(new ModelViewGLFeature(cam), true);
    }
    else
    {
      f.setMatrix(cam.getModelViewMatrix44D());
    }
  
    f = (ModelViewGLFeature) _glStateTransparent.getGLFeature(GLFeatureID.GLF_MODEL_VIEW);
    if (f == null)
    {
      _glStateTransparent.addGLFeature(new ModelViewGLFeature(cam), true);
    }
    else
    {
      f.setMatrix(cam.getModelViewMatrix44D());
    }
  
  }

  private java.util.ArrayList<LoadQueueItem> _loadQueue = new java.util.ArrayList<LoadQueueItem>();

  private void drainLoadQueue()
  {
  
    final int loadQueueSize = _loadQueue.size();
    for (int i = 0; i < loadQueueSize; i++)
    {
      LoadQueueItem item = _loadQueue.get(i);
      requestBuffer(item._url, item._priority, item._timeToCache, item._readExpired, item._uriPrefix, item._isTransparent, item._position, item._altitudeMode, item._listener, item._deleteListener, item._isBSON);
  
      if (item != null)
         item.dispose();
    }
  
    _loadQueue.clear();
  }


  private void requestBuffer(URL url, long priority, TimeInterval timeToCache, boolean readExpired, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener, boolean deleteListener, boolean isBSON)
  {
  
    IDownloader downloader = _context.getDownloader();
    downloader.requestBuffer(url, priority, timeToCache, readExpired, new ShapesRenderer_SceneJSBufferDownloadListener(this, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener, _context.getThreadUtils(), isBSON), true);
  
  }


  protected GEOTileRasterizer _geoTileRasterizer;



  public ShapesRenderer()
  {
     this(true);
  }
  public ShapesRenderer(boolean renderNotReadyShapes)
  {
     _renderNotReadyShapes = renderNotReadyShapes;
     _context = null;
     _glState = new GLState();
     _glStateTransparent = new GLState();
     _lastCamera = null;
     _autoDeleteShapeTouchListener = false;
     _shapeTouchListener = null;
     _geoTileRasterizer = null;
  }

  public ShapesRenderer(GEOTileRasterizer geoTileRasterizer)
  {
     _geoTileRasterizer = geoTileRasterizer;
     _renderNotReadyShapes = true;
     _context = null;
     _glState = new GLState();
     _glStateTransparent = new GLState();
     _lastCamera = null;
     _autoDeleteShapeTouchListener = false;
     _shapeTouchListener = null;
  }


  public void dispose()
  {
    final int shapesCount = _shapes.size();
    for (int i = 0; i < shapesCount; i++)
    {
      Shape shape = _shapes.get(i);
      if (shape != null)
         shape.dispose();
    }

    _glState._release();
    _glStateTransparent._release();

    if (_autoDeleteShapeTouchListener)
    {
      if (_shapeTouchListener != null)
         _shapeTouchListener.dispose();
    }
    _shapeTouchListener = null;


    super.dispose();

  }

  public void addShape(Shape shape)
  {
    _shapes.add(shape);
    if (_context != null)
    {
      shape.initialize(_context);
    }
    if (_geoTileRasterizer != null)
    {
      GEORasterSymbol geoRasterSymbol = shape.createRasterSymbolIfNeeded();
      if (geoRasterSymbol != null)
        _geoTileRasterizer.addSymbol(geoRasterSymbol);
    }
  }

  public final void removeShape(Shape shape)
  {
    int pos = -1;
    final int size = _shapes.size();
    for (int i = 0; i < size; i++)
    {
      if (_shapes.get(i) == shape)
      {
        pos = i;
        break;
      }
    }
    if (pos != -1)
    {
      _shapes.remove(pos);
    }
  }

  public final void removeAllShapes()
  {
     removeAllShapes(true);
  }
  public final void removeAllShapes(boolean deleteShapes)
  {
    if (deleteShapes)
    {
      final int shapesCount = _shapes.size();
      for (int i = 0; i < shapesCount; i++)
      {
        Shape shape = _shapes.get(i);
        if (shape != null)
           shape.dispose();
      }
    }
  
    _shapes.clear();
  }

  public final void enableAll()
  {
    final int shapesCount = _shapes.size();
    for (int i = 0; i < shapesCount; i++)
    {
      Shape shape = _shapes.get(i);
      shape.setEnable(true);
    }
  }

  public final void disableAll()
  {
    final int shapesCount = _shapes.size();
    for (int i = 0; i < shapesCount; i++)
    {
      Shape shape = _shapes.get(i);
      shape.setEnable(false);
    }
  }


  public final void onResume(G3MContext context)
  {
    _context = context;
  }

  public final void onPause(G3MContext context)
  {

  }

  public final void onDestroy(G3MContext context)
  {

  }

  public final void initialize(G3MContext context)
  {
    _context = context;
  
    if (_context != null)
    {
      final int shapesCount = _shapes.size();
      for (int i = 0; i < shapesCount; i++)
      {
        Shape shape = _shapes.get(i);
        shape.initialize(context);
      }
  
      drainLoadQueue();
    }
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    if (!_renderNotReadyShapes)
    {
      final int shapesCount = _shapes.size();
      for (int i = 0; i < shapesCount; i++)
      {
        Shape shape = _shapes.get(i);
        final boolean shapeReady = shape.isReadyToRender(rc);
        if (!shapeReady)
        {
          return RenderState.busy();
        }
      }
    }
    return RenderState.ready();
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    boolean handled = false;
    if (_lastCamera != null)
    {
      if (touchEvent.getTouchCount() == 1 && touchEvent.getTapCount()<=1 && touchEvent.getType() == TouchEventType.Down)
      {
        final Vector2I pixel = touchEvent.getTouch(0).getPos();
        java.util.ArrayList<ShapeDistance> shapeDistances = intersectionsDistances(ec.getPlanet(), _lastCamera, pixel);
  
        if (!shapeDistances.isEmpty())
        {
          //printf ("Found %d intersections with shapes:\n", (int)shapeDistances.size());
          if (_shapeTouchListener != null)
              handled = _shapeTouchListener.touchedShape(shapeDistances.get(0)._shape);
        }
  
      }
    }
    return handled;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
  }

  public final void start(G3MRenderContext rc)
  {
  }

  public final void stop(G3MRenderContext rc)
  {
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    // Saving camera for use in onTouchEvent
    _lastCamera = rc.getCurrentCamera();
  
    final Vector3D cameraPosition = rc.getCurrentCamera().getCartesianPosition();
  
    //Setting camera matrixes
    updateGLState(rc);
  
    _glState.setParent(glState);
    _glStateTransparent.setParent(glState);
  
  
    final int shapesCount = _shapes.size();
    for (int i = 0; i < shapesCount; i++)
    {
      Shape shape = _shapes.get(i);
      if (shape.isEnable())
      {
        if (shape.isTransparent(rc))
        {
          final Planet planet = rc.getPlanet();
          final Vector3D shapePosition = planet.toCartesian(shape.getPosition());
          final double squaredDistanceFromEye = shapePosition.sub(cameraPosition).squaredLength();
  
          rc.addOrderedRenderable(new TransparentShapeWrapper(shape, squaredDistanceFromEye, _glStateTransparent, _renderNotReadyShapes));
        }
        else
        {
          shape.render(rc, _glState, _renderNotReadyShapes);
        }
      }
    }
  }

  public final java.util.ArrayList<ShapeDistance> intersectionsDistances(Planet planet, Camera camera, Vector2I pixel)
  {
    final Vector3D origin = camera.getCartesianPosition();
    final Vector3D direction = camera.pixel2Ray(pixel);
    java.util.ArrayList<ShapeDistance> shapeDistances = new java.util.ArrayList<ShapeDistance>();
    for (int n = 0; n<_shapes.size(); n++)
    {
      Shape shape = _shapes.get(n);
      java.util.ArrayList<Double> distances = shape.intersectionsDistances(planet, camera, origin, direction);
      for (int i = 0; i<distances.size(); i++)
      {
        shapeDistances.add(new ShapeDistance(distances.get(i), shape));
      }
    }
  
    // sort vector
    java.util.Collections.sort(shapeDistances,
                               new java.util.Comparator<ShapeDistance>() {
                                 @Override
                                 public int compare(final ShapeDistance sd1,
                                                    final ShapeDistance sd2) {
                                   return Double.compare(sd1._distance, sd2._distance);
                                 }
                               });
  
    return shapeDistances;
  }

  public final void loadJSONSceneJS(URL url, long priority, TimeInterval timeToCache, boolean readExpired, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener)
  {
     loadJSONSceneJS(url, priority, timeToCache, readExpired, uriPrefix, isTransparent, position, altitudeMode, listener, true);
  }
  public final void loadJSONSceneJS(URL url, long priority, TimeInterval timeToCache, boolean readExpired, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode)
  {
     loadJSONSceneJS(url, priority, timeToCache, readExpired, uriPrefix, isTransparent, position, altitudeMode, null, true);
  }
  public final void loadJSONSceneJS(URL url, long priority, TimeInterval timeToCache, boolean readExpired, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener, boolean deleteListener)
  {
    if (_context == null)
    {
      _loadQueue.add(new LoadQueueItem(url, priority, timeToCache, readExpired, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener, false)); // isBson
    }
    else
    {
      requestBuffer(url, priority, timeToCache, readExpired, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener, false); // isBson
    }
  }

  public final void loadJSONSceneJS(URL url, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener)
  {
     loadJSONSceneJS(url, uriPrefix, isTransparent, position, altitudeMode, listener, true);
  }
  public final void loadJSONSceneJS(URL url, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode)
  {
     loadJSONSceneJS(url, uriPrefix, isTransparent, position, altitudeMode, null, true);
  }
  public final void loadJSONSceneJS(URL url, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener, boolean deleteListener)
  {
    loadJSONSceneJS(url, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener);
  }

  public final void loadBSONSceneJS(URL url, long priority, TimeInterval timeToCache, boolean readExpired, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener)
  {
     loadBSONSceneJS(url, priority, timeToCache, readExpired, uriPrefix, isTransparent, position, altitudeMode, listener, true);
  }
  public final void loadBSONSceneJS(URL url, long priority, TimeInterval timeToCache, boolean readExpired, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode)
  {
     loadBSONSceneJS(url, priority, timeToCache, readExpired, uriPrefix, isTransparent, position, altitudeMode, null, true);
  }
  public final void loadBSONSceneJS(URL url, long priority, TimeInterval timeToCache, boolean readExpired, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener, boolean deleteListener)
  {
    if (_context == null)
    {
      _loadQueue.add(new LoadQueueItem(url, priority, timeToCache, readExpired, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener, true)); // isBson
    }
    else
    {
      requestBuffer(url, priority, timeToCache, readExpired, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener, true); // isBson
    }
  }

  public final void loadBSONSceneJS(URL url, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener)
  {
     loadBSONSceneJS(url, uriPrefix, isTransparent, position, altitudeMode, listener, true);
  }
  public final void loadBSONSceneJS(URL url, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode)
  {
     loadBSONSceneJS(url, uriPrefix, isTransparent, position, altitudeMode, null, true);
  }
  public final void loadBSONSceneJS(URL url, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener, boolean deleteListener)
  {
    loadBSONSceneJS(url, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener);
  }

  public final void setShapeTouchListener(ShapeTouchListener shapeTouchListener, boolean autoDelete)
  {
    if (_autoDeleteShapeTouchListener)
    {
      if (_shapeTouchListener != null)
         _shapeTouchListener.dispose();
    }
  
    _shapeTouchListener = shapeTouchListener;
    _autoDeleteShapeTouchListener = autoDelete;
  }

}