package org.glob3.mobile.generated; 
//
//  SGNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

//
//  SGNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//




//class G3MContext;
//class G3MRenderContext;
//class SGShape;
//class GLGlobalState;
//class GPUProgramState;


public class SGNode
{
  protected final String _id;
  protected final String _sId;

  //  SGNode*              _parent;
  protected java.util.ArrayList<SGNode> _children = new java.util.ArrayList<SGNode>();


  //  void setParent(SGNode* parent);

  protected G3MContext _context;

  protected SGShape _shape;


  public SGNode(String id, String sId)
  //  _parent(NULL)
  {
     _id = id;
     _sId = sId;
     _context = null;
     _shape = null;
  }


  ///#include "GPUProgramState.hpp"
  
  
  public void dispose()
  {
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      SGNode child = _children.get(i);
      if (child != null)
         child.dispose();
    }
  }

  public void initialize(G3MContext context, SGShape shape)
  {
    _context = context;
    _shape = shape;
  
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      SGNode child = _children.get(i);
      child.initialize(context, shape);
    }
  }

  public final void addNode(SGNode child)
  {
    //  child->setParent(this);
    _children.add(child);
    if (_context != null)
    {
      child.initialize(_context, _shape);
    }
  }

  public boolean isReadyToRender(G3MRenderContext rc)
  {
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      SGNode child = _children.get(i);
      if (!child.isReadyToRender(rc))
      {
        return false;
      }
    }
  
    return true;
  }

  public void prepareRender(G3MRenderContext rc)
  {
  
  }

  public void cleanUpRender(G3MRenderContext rc)
  {
  
  }

  public void render(G3MRenderContext rc, GLState parentGLState, boolean renderNotReadyShapes)
  {
  
  //  ILogger::instance()->logInfo("Rendering SG: " + description());
  
    final GLState glState = createState(rc, parentGLState);
    if (glState != null)
    {
  
      prepareRender(rc);
  
      rawRender(rc, glState);
  
      final int childrenCount = _children.size();
      for (int i = 0; i < childrenCount; i++)
      {
        SGNode child = _children.get(i);
        child.render(rc, glState, renderNotReadyShapes);
      }
  
      cleanUpRender(rc);
    }
    else
    {
      ILogger.instance().logError("NO GLSTATE");
    }
  }

  public GLState createState(G3MRenderContext rc, GLState parentState)
  {
     return parentState;
  }

  public final int getChildrenCount()
  {
    return _children.size();
  }

  public final SGNode getChild(int i)
  {
    return _children.get(i);
  }

  public void rawRender(G3MRenderContext rc, GLState parentGLState)
  {
  }

  public String description()
  {
    return "SGNode";
  }

  public Vector3D mostDistantVertexFromCenter(MutableMatrix44D transformation)
  {
    double max = 0;
    MutableVector3D res = new MutableVector3D(0, 0, 0);
    final int s = getChildrenCount();
    for (int i = 0; i < s; i++)
    {
      Vector3D v = getChild(i).mostDistantVertexFromCenter(transformation);
      double d = v.squaredLength();
      if (max < d)
      {
        max = d;
        res.copyFrom(v);
      }
    }
    return res.asVector3D();
  }

  public void centerGeometryAtZero()
  {
  }

  public Vector3D getMax(MutableMatrix44D transformation)
  {
    MutableVector3D res = new MutableVector3D(-9e99,-9e99,-9e99);
    final int s = getChildrenCount();
    for (int i = 0; i < s; i++)
    {
      Vector3D v = getChild(i).getMax(transformation);
      res.copyFrom(Vector3D.maxOnAllAxis(v, res.asVector3D()));
    }
    return res.asVector3D();
  }

  public Vector3D getMin(MutableMatrix44D transformation)
  {
    MutableVector3D res = new MutableVector3D(9e99, 9e99, 9e99);
    final int s = getChildrenCount();
    for (int i = 0; i < s; i++)
    {
      Vector3D v = getChild(i).getMin(transformation);
      res.copyFrom(Vector3D.minOnAllAxis(v, res.asVector3D()));
    }
    return res.asVector3D();
  }

  public final Vector3D getCenter(MutableMatrix44D transformation)
  {
    return Vector3D.meanOnAllAxis(getMax(transformation), getMin(transformation));
  }

}