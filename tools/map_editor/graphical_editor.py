import Tkinter as tk
from PIL import Image, ImageTk, ImageDraw

class GraphicalEditor:
  """Graphical map editor. Renders map data visually using Tkinter."""

  MAP_FILE = 'map_2400.png'

  # Configuration
  LOCATION_RADIUS = 6
  PASSIVE_LOCATION_RADIUS = 4
  LOCATION_COLOR = 'blue'
  PASSIVE_LOCATION_COLOR = 'red'
  PATH_COLOR = 'black'

  def __init__(self, map_data):
    self.map_data = map_data

    map_image = Image.open(self.MAP_FILE)
    map_image.thumbnail(map_image.size,Image.ANTIALIAS)

    self._draw_map(map_image)

    # Draw image on GUI
    root = tk.Tk()
    tk_image = ImageTk.PhotoImage(map_image)
    panel1 = tk.Label(root, image=tk_image)
    panel1.pack(side='left', fill='both', expand='yes')

    root.bind("<1>", self._mousedown)
    root.mainloop()

  def _draw_map(self, map_image):
    draw = ImageDraw.Draw(map_image)

    for loc in self.map_data.locations:
      coord = GraphicalEditor._conv(loc[1],loc[2])
      r = self.LOCATION_RADIUS
      draw.ellipse((coord[0]-r,coord[1]-r,coord[0]+r,coord[1]+r),fill=self.LOCATION_COLOR)

    for loc in self.map_data.passive_locations:
      coord = GraphicalEditor._conv(loc[1],loc[2])
      r = self.PASSIVE_LOCATION_RADIUS
      draw.ellipse((coord[0]-r,coord[1]-r,coord[0]+r,coord[1]+r),fill=self.PASSIVE_LOCATION_COLOR)
      draw.text((coord[0],coord[1]),loc[0],(0,0,0))

    for path in self.map_data.paths:
      coord1 = GraphicalEditor._conv(path[0][0],path[0][1])
      coord2 = GraphicalEditor._conv(path[1][0],path[1][1])
      draw.line((coord1[0],coord1[1],coord2[0],coord2[1]),fill=self.PATH_COLOR)

  def _mousedown(self, event):
    """Handle mouse click"""
    # Print map coordinates of location clicked
    (cx,cy) = GraphicalEditor._conv2(event.x,event.y)
    print "  p",int(cx),int(cy)

  @staticmethod
  def _conv(x, y):
    """converts original pixel to displayed (large)"""
    original_ratio = 0.755905512
    offset_x = 1195
    offset_y = 872
    return ((x-offset_x)*original_ratio,(y-offset_y)*original_ratio)

  @staticmethod
  def _conv2(x, y):
    """opposite of conv"""
    original_ratio = 0.755905512
    offset_x = 1195
    offset_y = 872
    return (x/original_ratio + offset_x, y/original_ratio + offset_y)
