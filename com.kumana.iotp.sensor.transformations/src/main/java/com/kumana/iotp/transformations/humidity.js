var transform = function(value){
	var raw = 0.01;
	raw = parseFloat(value+"");
	var hum = (raw/65535)*100;
	return hum+"";
}