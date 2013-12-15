function showLoadingDiv(text){
	$("#loadingText").text(text);
	$("#loadingDiv").show();
}
function hideLoadingDiv(){
	$("#loadingDiv").hide();
}
function isEmpty(o){
	if( Object.prototype.toString.call( o ) === '[object Array]' && o.length == 1){
		return $.isEmptyObject(o[0]);
	}else{
		return $.isEmptyObject(o);
	}
}

function containsInStringList(list, e){
	return !isEmpty(list) && list.indexOf(e) !== -1;
}
function removeFromStringList(list, e){
	if(!isEmpty(list)){
		list = list.replace(new RegExp(_separator_+e+"$|^"+e+"("+_separator_+")?"),"");
		list = list.replace(new RegExp(_separator_+e+_separator_),_separator_);
	}
	return list;
}
function addToStringList(list, e){
	if(isEmpty(list)){
		list = list + e;
	}else{
		list = list + _separator_ + e;
	}
	return list;
}

$(function(){
	$( "input[type=submit],input[type=button], a.button, button" ).button();
});

/**
 * Protect window.console method calls, e.g. console is not defined on IE
 * unless dev tools are open, and IE doesn't define console.debug
 */
(function() {
  if (!window.console) {
    window.console = {};
  }
  // union of Chrome, FF, IE, and Safari console methods
  var m = [
    "log", "info", "warn", "error", "debug", "trace", "dir", "group",
    "groupCollapsed", "groupEnd", "time", "timeEnd", "profile", "profileEnd",
    "dirxml", "assert", "count", "markTimeline", "timeStamp", "clear"
  ];
  // define undefined methods as noops to prevent errors
  for (var i = 0; i < m.length; i++) {
    if (!window.console[m[i]]) {
      window.console[m[i]] = function() {};
    }    
  } 
})();