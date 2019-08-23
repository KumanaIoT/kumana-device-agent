var transform = function(value){
	var raw = 0.01;
	raw = parseFloat(value+"");
	var bat = (raw/65535)*25;
	return bat;
}