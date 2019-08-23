var transform = function (value){
	var raw = 0.01;
	var error = 37.4937056534676;
	raw = parseFloat(value+"");
	var cur = (raw/65535)*300-150-error;
	return cur+"";
}