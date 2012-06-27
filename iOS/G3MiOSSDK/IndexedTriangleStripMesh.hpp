//
//  IndexedTriangleStripMesh.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IndexedTriangleStripMesh_h
#define G3MiOSSDK_IndexedTriangleStripMesh_h

#include "Mesh.hpp"
#include "Color.hpp"
#include "Vector2D.hpp"
#include "Vector3D.hpp"


class IndexedTriangleStripMesh : public Mesh {
  
  const unsigned char * _indexes;
  const float * _vertices;
  const int _numIndex;
  
  const float * _texCoords;
  const float * _normals;
  const Color _color;
  const int _textureId;
  const bool _owner;
  
public:
  
  ~IndexedTriangleStripMesh();
  
  //NOT TEXTURED
  IndexedTriangleStripMesh(bool owner,
                           const float* vertices,
                           const unsigned char* indexes,
                           const int numIndex, 
                           const Color& color,
                           const float* normals = NULL);
  
  IndexedTriangleStripMesh(const std::vector<Vector3D>& vertices,
                           const std::vector<unsigned char>& indexes,
                           const Color& color,
                           const std::vector<Vector3D>* normals = NULL);
  
  //TEXTURED
  IndexedTriangleStripMesh(bool owner,
                           const float* vertices,
                           const unsigned char* indexes,
                           const int numIndex, 
                           const int texID,
                           const float* texCoords,
                           const float* normals = NULL);
  
  IndexedTriangleStripMesh(const std::vector<Vector3D>& vertices,
                           const std::vector<unsigned char>& indexes,
                           const int texID,
                           const std::vector<Vector2D>& texCoords,
                           const std::vector<Vector3D>* normals = NULL);
  
  virtual void render(const RenderContext* rc) const;
  
};

#endif
