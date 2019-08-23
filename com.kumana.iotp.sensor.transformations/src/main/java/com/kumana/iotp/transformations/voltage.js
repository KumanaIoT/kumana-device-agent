var transform = function(value){
	var raw =0.01;
	raw = parseFloat(value+"");	
	var vol = (raw/65535)*40;
	return vol+"";
}