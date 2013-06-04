function showLoadingDiv(text){
	$("#loadingText").text(text);
	$("#loadingDiv").show();
}

function isEmpty(o){
	if( Object.prototype.toString.call( o ) === '[object Array]' && o.length == 1){
		return $.isEmptyObject(o[0]);
	}else{
		return $.isEmptyObject(o);
	}
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