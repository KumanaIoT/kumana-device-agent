var transform = function(value){
	var raw = 0.01;
	raw = parseFloat(value+"");
	var lux = 100-((raw/65535)*100);
	return lux+"";
}