package org.glob3.mobile.generated; 
//
//  MeshRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/22/12.
//
//

//
//  MeshRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/22/12.
//
//



//class Mesh;


public class MeshRenderer extends LeafRenderer
{
  private java.util.ArrayList<Mesh> _meshes = new java.util.ArrayList<Mesh>();

  private GLState _glState;

//  ProjectionGLFeature* _projection;
//  ModelGLFeature*      _model;
  private void updateGLState(G3MRenderContext rc)
  {
  
    final Camera cam = rc.getCurrentCamera();
  //  if (_projection == NULL) {
  //    _projection = new ProjectionGLFeature(cam);
  //    _glState->addGLFeature(_projection, true);
  //  } else{
  //    _projection->setMatrix(cam->getProjectionMatrix44D());
  //  }
  //
  //  if (_model == NULL) {
  //    _model = new ModelGLFeature(cam->getModelMatrix44D());
  //    _glState->addGLFeature(_model, true);
  //  } else{
  //    _model->setMatrix(cam->getModelMatrix44D());
  //  }
  
    ModelViewGLFeature f = (ModelViewGLFeature) _glState.getGLFeature(GLFeatureID.GLF_MODEL_VIEW);
    if (f == null)
    {
      _glState.addGLFeature(new ModelViewGLFeature(cam), true);
    }
    else
    {
      f.setMatrix(cam.getModelViewMatrix44D());
    }
  }

  public MeshRenderer()
//  _projection(NULL),
//  _model(NULL),
  {
     _glState = new GLState();
  }

  public void dispose()
  {
    final int meshesCount = _meshes.size();
    for (int i = 0; i < meshesCount; i++)
    {
      Mesh mesh = _meshes.get(i);
      if (mesh != null)
         mesh.dispose();
    }
  
    _glState._release();
  
    super.dispose();
  
  }

  public final void addMesh(Mesh mesh)
  {
    _meshes.add(mesh);
  }

  public final void clearMeshes()
  {
    final int meshesCount = _meshes.size();
    for (int i = 0; i < meshesCount; i++)
    {
      Mesh mesh = _meshes.get(i);
      if (mesh != null)
         mesh.dispose();
    }
    _meshes.clear();
  }

  public final void onResume(G3MContext context)
  {

  }

  public final void onPause(G3MContext context)
  {

  }

  public final void onDestroy(G3MContext context)
  {

  }

  public final void initialize(G3MContext context)
  {

  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return true;
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    final Frustum frustum = rc.getCurrentCamera().getFrustumInModelCoordinates();
    updateGLState(rc);
  
    _glState.setParent(glState);
  
  
    final int meshesCount = _meshes.size();
    for (int i = 0; i < meshesCount; i++)
    {
      Mesh mesh = _meshes.get(i);
      final BoundingVolume boundingVolume = mesh.getBoundingVolume();
      if (boundingVolume.touchesFrustum(frustum))
      {
        mesh.render(rc, _glState);
      }
    }
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    return false;
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

}