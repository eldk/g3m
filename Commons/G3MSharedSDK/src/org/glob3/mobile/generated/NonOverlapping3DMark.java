package org.glob3.mobile.generated; 
public class NonOverlapping3DMark
{

    /*added things here:*/

    //unlike for 2D, not every node has to have an anchor, so make 3D mark a node, where some
    //may be anchors and some may not be.
    private boolean _isAnchor;
    private boolean _isVisited; //for graph traversals

    //shape for anchor and regular nodes:
    private Shape _anchorShape;
    private Shape _nodeShape;
    private ShapesRenderer _shapesRenderer;

    //nodes can have multiple nodes they are attached to, call these neighbors
    //edges go from this node to neighbor nodes
    private java.util.ArrayList<NonOverlapping3DMark> _neighbors = new java.util.ArrayList<NonOverlapping3DMark>();
    private NonOverlapping3DMark _anchor; //anchor also included in neighbors. node can only have one anchor

    private float _springLengthInPixels;

    private Vector3D _cartesianPos;
    private Geodetic3D _geoPosition ;

    private float _dX; //Velocity vector (pixels per second)
    private float _dY;
    private float _dZ;
    private float _fX; //Applied Force
    private float _fY;
    private float _fZ;

    private final float _springK;
    private final float _maxSpringLength;
    private final float _minSpringLength;
    private final float _electricCharge;
    private final float _maxWidgetSpeedInPixelsPerSecond;
    private final float _resistanceFactor;
    private final float _minWidgetSpeedInPixelsPerSecond;



    public NonOverlapping3DMark(Shape anchorShape, Shape nodeShape, Geodetic3D position, ShapeTouchListener touchListener, float springLengthInPixels, float springK, float maxSpringLength, float minSpringLength, float electricCharge, float maxWidgetSpeedInPixelsPerSecond, float minWidgetSpeedInPixelsPerSecond)
    {
       this(anchorShape, nodeShape, position, touchListener, springLengthInPixels, springK, maxSpringLength, minSpringLength, electricCharge, maxWidgetSpeedInPixelsPerSecond, minWidgetSpeedInPixelsPerSecond, 0.95f);
    }
    public NonOverlapping3DMark(Shape anchorShape, Shape nodeShape, Geodetic3D position, ShapeTouchListener touchListener, float springLengthInPixels, float springK, float maxSpringLength, float minSpringLength, float electricCharge, float maxWidgetSpeedInPixelsPerSecond)
    {
       this(anchorShape, nodeShape, position, touchListener, springLengthInPixels, springK, maxSpringLength, minSpringLength, electricCharge, maxWidgetSpeedInPixelsPerSecond, 35.0f, 0.95f);
    }
    public NonOverlapping3DMark(Shape anchorShape, Shape nodeShape, Geodetic3D position, ShapeTouchListener touchListener, float springLengthInPixels, float springK, float maxSpringLength, float minSpringLength, float electricCharge)
    {
       this(anchorShape, nodeShape, position, touchListener, springLengthInPixels, springK, maxSpringLength, minSpringLength, electricCharge, 1000.0f, 35.0f, 0.95f);
    }
    public NonOverlapping3DMark(Shape anchorShape, Shape nodeShape, Geodetic3D position, ShapeTouchListener touchListener, float springLengthInPixels, float springK, float maxSpringLength, float minSpringLength)
    {
       this(anchorShape, nodeShape, position, touchListener, springLengthInPixels, springK, maxSpringLength, minSpringLength, 3000.0f, 1000.0f, 35.0f, 0.95f);
    }
    public NonOverlapping3DMark(Shape anchorShape, Shape nodeShape, Geodetic3D position, ShapeTouchListener touchListener, float springLengthInPixels, float springK, float maxSpringLength)
    {
       this(anchorShape, nodeShape, position, touchListener, springLengthInPixels, springK, maxSpringLength, 5.0f, 3000.0f, 1000.0f, 35.0f, 0.95f);
    }
    public NonOverlapping3DMark(Shape anchorShape, Shape nodeShape, Geodetic3D position, ShapeTouchListener touchListener, float springLengthInPixels, float springK)
    {
       this(anchorShape, nodeShape, position, touchListener, springLengthInPixels, springK, 100.0f, 5.0f, 3000.0f, 1000.0f, 35.0f, 0.95f);
    }
    public NonOverlapping3DMark(Shape anchorShape, Shape nodeShape, Geodetic3D position, ShapeTouchListener touchListener, float springLengthInPixels)
    {
       this(anchorShape, nodeShape, position, touchListener, springLengthInPixels, 1.0f, 100.0f, 5.0f, 3000.0f, 1000.0f, 35.0f, 0.95f);
    }
    public NonOverlapping3DMark(Shape anchorShape, Shape nodeShape, Geodetic3D position, ShapeTouchListener touchListener)
    {
       this(anchorShape, nodeShape, position, touchListener, 10.0f, 1.0f, 100.0f, 5.0f, 3000.0f, 1000.0f, 35.0f, 0.95f);
    }
    public NonOverlapping3DMark(Shape anchorShape, Shape nodeShape, Geodetic3D position)
    {
       this(anchorShape, nodeShape, position, null, 10.0f, 1.0f, 100.0f, 5.0f, 3000.0f, 1000.0f, 35.0f, 0.95f);
    }
    public NonOverlapping3DMark(Shape anchorShape, Shape nodeShape, Geodetic3D position, ShapeTouchListener touchListener, float springLengthInPixels, float springK, float maxSpringLength, float minSpringLength, float electricCharge, float maxWidgetSpeedInPixelsPerSecond, float minWidgetSpeedInPixelsPerSecond, float resistanceFactor)
    {
       _geoPosition = new Geodetic3D(position);
       _springLengthInPixels = springLengthInPixels;
       _cartesianPos = null;
       _dX = 0F;
       _dY = 0F;
       _dZ = 0F;
       _fX = 0F;
       _fY = 0F;
       _fZ = 0F;
       _springK = springK;
       _maxSpringLength = maxSpringLength;
       _minSpringLength = minSpringLength;
       _electricCharge = electricCharge;
       _maxWidgetSpeedInPixelsPerSecond = maxWidgetSpeedInPixelsPerSecond;
       _resistanceFactor = resistanceFactor;
       _minWidgetSpeedInPixelsPerSecond = minWidgetSpeedInPixelsPerSecond;
       _anchorShape = anchorShape;
       _nodeShape = nodeShape;
        _shapesRenderer = new ShapesRenderer();
        _shapesRenderer.addShape(nodeShape);
        _nodeShape.setPosition(_geoPosition);
        _anchorShape.setPosition(_geoPosition);
    }

    public void dispose()
    {
        if (_cartesianPos != null)
           _cartesianPos.dispose();
    }
    public final Geodetic3D getPos()
    {
        return _geoPosition;
    }

    public final boolean isVisited()
    {
        return _isVisited;
    }
    public final boolean isAnchor()
    {
        return _isAnchor;
    }
    public final java.util.ArrayList<NonOverlapping3DMark> getNeighbors()
    {
        return _neighbors;
    }
    public final void setVisited(boolean visited)
    {
        _isVisited = visited;
    }
    public final void addEdge(NonOverlapping3DMark n)
    {
        _neighbors.add(n);
         n.addNeighbor(n);
    }
    public final void setAsAnchor()
    {
        _isAnchor = true;
        _shapesRenderer.removeAllShapes();
        _shapesRenderer.addShape(_anchorShape);
    }
    public final void addNeighbor(NonOverlapping3DMark n)
    {
        _neighbors.add(n);
    }
    public final void addAnchor(NonOverlapping3DMark anchor)
    {
        _neighbors.add(anchor);
        anchor.addNeighbor(this);
        _anchor = anchor;
        anchor.setAsAnchor();
    }
    public final NonOverlapping3DMark getAnchor()
    {
        return _anchor;
    }
    public final Shape getShape()
    {
        if(_isAnchor)
           return _anchorShape;
        else
           return _nodeShape;
    }
    //MarkWidget getWidget() const;

    public final Vector3D clampVector(Vector3D v, float min, float max)
    {
        float l = v.length();
        if(l < min)
        {
            return (v.normalized()).times(min);
        }
        if(l > max)
        {
            return (v.normalized()).times(max);
        }
    }

    public final Vector3D getCartesianPosition(Planet planet)
    {
        if (_cartesianPos == null)
        {
            _cartesianPos = new Vector3D(planet.toCartesian(_geoPosition));
        }
        return _cartesianPos;
    }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    void computeAnchorScreenPos(Camera cam, Planet planet);

    //Vector2F getScreenPos() const{ return _widget.getScreenPos();}
    //Vector2F getAnchorScreenPos() const{ return _anchorWidget.getScreenPos();}

    public final void render(G3MRenderContext rc, GLState glState)
    {
        _shapesRenderer.render(rc, glState);
    }

    public final void applyCoulombsLaw(NonOverlapping3DMark that, Planet planet)
    {
        //get 3d positionf or both
        Vector3D d = getCartesianPosition(planet).sub(that.getCartesianPosition(planet)).normalized();
        float distance = d.length();
        Vector3D direction = d.normalized();
    
        float strength = (float)(this._electricCharge * that._electricCharge/distance *distance);
        Vector3D force = direction.times(strength);
    
        this.applyForce(force._x, force._y, force._z);
        that.applyForce(-force._x, -force._y, -force._z);
    }
    public final void applyCoulombsLawFromAnchor(NonOverlapping3DMark that, Planet planet)
    {
    
        Vector3D dAnchor = getCartesianPosition(planet).sub(that.getCartesianPosition(planet));
    
        double distanceAnchor = dAnchor.length() + 0.001;
        Vector3D directionAnchor = dAnchor.div((float) distanceAnchor);
    
        float strengthAnchor = (float)(this._electricCharge * that._electricCharge / (distanceAnchor * distanceAnchor));
    
        this.applyForce(directionAnchor._x * strengthAnchor, directionAnchor._y * strengthAnchor, directionAnchor._z);
    }

    public final void applyHookesLaw(Planet planet)
    {
        if (getAnchor() != null)
        {
            Vector3D d = getCartesianPosition(planet).sub(getAnchor().getCartesianPosition(planet));
            double mod = d.length();
            double displacement = _springLengthInPixels - mod;
            Vector3D direction = d.div((float)mod);
    
            float force = (float)(_springK * displacement);
    
            applyForce((float)(direction._x * force), (float)(direction._y * force), (float) direction._z * force);
        }
    }

    public final void applyForce(float x, float y)
    {
        _fX += x;
        _fY += y;
    }

    public final void applyForce(float x, float y, float z)
    {
        _fX += x;
        _fY += y;
        _fZ += z;
    }

    public final void updatePositionWithCurrentForce(double elapsedMS, float viewportWidth, float viewportHeight, Planet planet)
    {
    
        Vector3D oldVelocity = new Vector3D(_dX, _dY, _dZ);
        Vector3D force = new Vector3D(_fX, _fY, _fZ);
    
        //Assuming Widget Mass = 1.0
        float time = (float)(elapsedMS / 1000.0);
        Vector3D velocity = oldVelocity.add(force.times(time)).times(_resistanceFactor); //Resistance force applied as x0.85
    
        //Force has been applied and must be reset
        _fX = 0F;
        _fY = 0F;
        _fZ = 0F;
    
        //Clamping Velocity
        double velocityPPS = velocity.length();
        if (velocityPPS > _maxWidgetSpeedInPixelsPerSecond)
        {
            _dX = (float)(velocity._x * (_maxWidgetSpeedInPixelsPerSecond / velocityPPS));
            _dY = (float)(velocity._y * (_maxWidgetSpeedInPixelsPerSecond / velocityPPS));
            _dZ = (float)(velocity._z * (_maxWidgetSpeedInPixelsPerSecond / velocityPPS));
    
        }
        else
        {
            if (velocityPPS < _minWidgetSpeedInPixelsPerSecond)
            {
                _dX = 0.0F;
                _dY = 0.0F;
                _dZ = 0.0F;
            }
            else
            {
                //Normal case
                _dX = (float)velocity._x;
                _dY = (float)velocity._y;
                _dZ = (float)velocity._z;
            }
        }
    
        //Update position
        //Vector2F position = _widget.getScreenPos();
        Vector3D position = getCartesianPosition(planet);
    
        float newX = position._x + (_dX * time);
        float newY = position._y + (_dY * time);
        float newZ = position._z + (_dZ * time);
    
        if(this.getAnchor() != null)
        {
        Vector3D anchorPos = getAnchor().getCartesianPosition(planet); //getScreenPos();
    
        Vector3D temp_spring = new Vector3D(newX,newY, newZ).sub(anchorPos);
        Vector3D spring = clampVector(temp_spring, _minSpringLength, _maxSpringLength);
        Vector3D finalPos = anchorPos.add(spring);
        _anchorShape.setTranslation(finalPos);
        _nodeShape.setTranslation(finalPos);
       // _widget.set3DPos(  finalPos._x, finalPos._y, finalPos._z);
        //_widget.clampPositionInsideScreen((int)viewportWidth, (int)viewportHeight, 5); // 5 pixels of margin
        }
    
        //TODO: update this with new graph stuffs
    
    }

    public final void onResizeViewportEvent(int width, int height)
    {
        //TODO
    }

    public final void resetShapePositionVelocityAndForce()
    {
         _dX = 0F;
         _dY = 0F;
         _dZ = 0F;
         _fX = 0F;
         _fY = 0F;
         _fZ = 0F;
        _anchorShape.setPosition(_geoPosition);
        _nodeShape.setPosition(_geoPosition);
     }

    public final boolean onTouchEvent(float x, float y)
    {
        //TODO
        return false;
    }

}