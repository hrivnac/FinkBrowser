function modifyMenu(hform) {
  hform.fields.push({field:'l', type: 'text', html: {caption: 'l Key', text : ' (exact search on row key)' , attr: 'style="width: 500px"'}});
  hform.record.l = 1;
  }
  
function modifyRequest(hform) {
  return "&l=" + hform.record.l;
  }