package org.glob3.mobile.generated; 
//
//  DebugTileRasterizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/8/13.
//
//

//
//  DebugTileRasterizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/8/13.
//
//



//class ICanvas;
//class Sector;

public class DebugTileRasterizer extends CanvasTileRasterizer
{

  private final GFont _font;
  private final Color _color ;

  private final boolean _showLabels;
  private final boolean _showTileBounds;

  private String getTileKeyLabel(Tile tile)
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("L:");
    isb.addInt(tile.getLevel());
  
    isb.addString(", C:");
    isb.addInt(tile.getColumn());
  
    isb.addString(", R:");
    isb.addInt(tile.getRow());
  
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  private String getSectorLabel1(Sector sector)
  {
    return "Lower lat: " + sector._lower._latitude.description();
  }
  private String getSectorLabel2(Sector sector)
  {
    return "Lower lon: " + sector._lower._longitude.description();
  }
  private String getSectorLabel3(Sector sector)
  {
    return "Upper lat: " + sector._upper._latitude.description();
  }
  private String getSectorLabel4(Sector sector)
  {
    return "Upper lon: " + sector._upper._longitude.description();
  }

  public DebugTileRasterizer()
  {
     _font = GFont.monospaced(15);
     _color = new Color(Color.yellow());
     _showLabels = true;
     _showTileBounds = true;
  }

  public DebugTileRasterizer(GFont font, Color color, boolean showLabels, boolean showTileBounds)
  {
     _font = font;
     _color = new Color(color);
     _showLabels = showLabels;
     _showTileBounds = showTileBounds;
  
  }

  public void dispose()
  {
    super.dispose();
  }

  public final String getId()
  {
    return "DebugTileRasterizer";
  }

  public final void rasterize(IImage image, TileRasterizerContext trc, IImageListener listener, boolean autodelete)
  {
  
    final Tile tile = trc._tile;
  
    final int width = image.getWidth();
    final int height = image.getHeight();
  
    ICanvas canvas = getCanvas(width, height);
  
    canvas.removeShadow();
  
    canvas.drawImage(image, 0, 0);
  
    if (_showTileBounds)
    {
      canvas.setLineColor(_color);
      canvas.setLineWidth(1);
      canvas.strokeRectangle(0, 0, width, height);
    }
  
    if (_showLabels)
    {
      canvas.setShadow(Color.black(), 2, 1, -1);
      ColumnCanvasElement col = new ColumnCanvasElement();
      col.add(new TextCanvasElement(getTileKeyLabel(tile), _font, _color));
  
      final Sector sectorTile = tile.getSector();
      col.add(new TextCanvasElement(getSectorLabel1(sectorTile), _font, _color));
      col.add(new TextCanvasElement(getSectorLabel2(sectorTile), _font, _color));
      col.add(new TextCanvasElement(getSectorLabel3(sectorTile), _font, _color));
      col.add(new TextCanvasElement(getSectorLabel4(sectorTile), _font, _color));
  
      final Vector2F colExtent = col.getExtent(canvas);
      col.drawAt((width - colExtent._x) / 2, (height - colExtent._y) / 2, canvas);
    }
  
    canvas.createImage(listener, autodelete);
  
    if (image != null)
       image.dispose();
  }

}