gwt-dnd-sample
==============

GWT Drag And Drop Sample with CellTable / DataGrid.
The sample is deployed to appengine : http://gwt-dnd-sample.appspot.com/

### Tips
To make a widget draggable, you have to :
* position the attribute "draggable" to "true" : widget.getElement().setDraggable(Element.DRAGGABLE_TRUE);
* listen to dragstart event and position the values you want to drag via event.getDataTransfer().setData("key","value");

To drop in a widget, you have to :
* listen to dragover event. Without it, the drop event is ever called!
* listen to drop event and read the dragged values via event.getDataTransfer().getData("key");
