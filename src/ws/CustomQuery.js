// Additions to the default menu

function modifyMenu(hform) {
  hform.fields.push({field:'latest', type: 'text', html: {caption: '* Latest', text : ' (show latest objects, time in minutes before now)' , attr: 'style="width: 100px"'}});
  hform.record.latest = '';
  }
  
function modifyRequest(hform) {
  return "&latest=" + hform.record.latest;
  }
  
// New search from the 'latests' alerts page
  
function searchDetails(url) {
  var dd = document.getElementsByClassName('details');
  var keys = "";
  for (var i = 0; i < dd.length; i++) {
    if (dd[i].checked) {
      keys += dd[i].value + ",";
      }
    }
  if (keys === "") {
    for (var i = 0; i < dd.length; i++) {
      keys += dd[i].value + ",";
      }
    }
  loadPane("result", url + "&krefix=" + keys);  
  }
  
// New ranges search
  
function searchRanges(url) {
  var ra  = document.getElementById('range-slider-ra').value;
  var dec = document.getElementById('range-slider-dec').value;
  var ff  = document.getElementById('ffselector').value;
  if (ff != "") {
    formula     = ff;
    formulaArgs = "";
    }
  else {
    formula     = "isWithinGeoLimits(" + ra + "," + dec + ")";
    formulaArgs = "ra,dec";
    }
  loadPane("result", url + "&formula=" + formula + "&formulaArgs=" + formulaArgs);  
  }
