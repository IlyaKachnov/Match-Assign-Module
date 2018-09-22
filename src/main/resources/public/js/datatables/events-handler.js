var DOMEvents = {
    AnimationEvent: "animationend animationiteration animationstart",
    MouseEvent: "click contextmenu dblclick mousedown mouseenter mouseleave mousemove mouseout mouseover mouseup show",
    KeyboardEvent: "keydown keypress keyup"
}

for(DOMEvent in DOMEvents){

  var DOMEventTypes = DOMEvents[DOMEvent].split(' ');

  DOMEventTypes.filter(function(DOMEventType){
    var DOMEventCategory = DOMEvent + ' '+DOMEventType;
    document.addEventListener(DOMEventType, function(e){
      $('[data-toggle="m-popover"]').popover();
    }, true);
  });

}