package org.glob3.mobile.generated; 
//
//  GEOMeshSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

//
//  GEOMeshSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//



//class Mesh;
//class Geodetic2D;
//class Color;
//class Ellipsoid;
//class Planet;

public abstract class GEOMeshSymbol extends GEOSymbol
{

  protected final Mesh createLine2DMesh(java.util.ArrayList<Geodetic2D> coordinates, Color lineColor, float lineWidth, double deltaHeight, Planet planet)
  {
    FloatBufferBuilderFromGeodetic vertices = new FloatBufferBuilderFromGeodetic(CenterStrategy.firstVertex(), planet, Geodetic2D.zero());
  
    final int coordinatesCount = coordinates.size();
    for (int i = 0; i < coordinatesCount; i++)
    {
      final Geodetic2D coordinate = coordinates.get(i);
      vertices.add(coordinate._latitude, coordinate._longitude, deltaHeight);
    }
  
    return new DirectMesh(GLPrimitive.lineStrip(), true, vertices.getCenter(), vertices.create(), lineWidth, 1, new Color(lineColor), null, 0.0f, false);
  }

  protected final Mesh createLines2DMesh(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray, Color lineColor, float lineWidth, double deltaHeight, Planet planet)
  {
  
    FloatBufferBuilderFromGeodetic vertices = new FloatBufferBuilderFromGeodetic(CenterStrategy.firstVertex(), planet, Geodetic2D.zero());
    ShortBufferBuilder indices = new ShortBufferBuilder();
  
    final int coordinatesArrayCount = coordinatesArray.size();
    short index = 0;
    for (int i = 0; i < coordinatesArrayCount; i++)
    {
      java.util.ArrayList<Geodetic2D> coordinates = coordinatesArray.get(i);
      final int coordinatesCount = coordinates.size();
      for (int j = 0; j < coordinatesCount; j++)
      {
        final Geodetic2D coordinate = coordinates.get(j);
  
        vertices.add(coordinate._latitude, coordinate._longitude, deltaHeight);
  
        indices.add(index);
        if ((j > 0) && (j < (coordinatesCount-1)))
        {
          indices.add(index);
        }
        index++;
      }
    }
  
    return new IndexedMesh(GLPrimitive.lines(), true, vertices.getCenter(), vertices.create(), indices.create(), lineWidth, 1, new Color(lineColor), null, 0.0f, false);
  }


  protected abstract Mesh createMesh(G3MRenderContext rc);


  public final void symbolize(G3MRenderContext rc, GEOSymbolizationContext sc)
  {
    MeshRenderer meshRenderer = sc.getMeshRenderer();
    if (meshRenderer == null)
    {
      ILogger.instance().logError("Can't simbolize with Mesh, MeshRenderer was not set");
    }
    else
    {
      Mesh mesh = createMesh(rc);
      if (mesh != null)
      {
        meshRenderer.addMesh(mesh);
      }
    }
  }


}