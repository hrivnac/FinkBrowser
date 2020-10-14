if (typeof hideHBaseSelector === 'undefined' || !hideHBaseSelector) {
  $(function() {
    $("#slider-ra").slider({
      orientation: "horizontal",
      range: true,
      min: 0,
      max: 180,
      values: [0, 180],
      slide: function(event, ui) {
        $("#amount-ra").val(ui.values[0] + " - " + ui.values[1]);
        }
      });
    $("#amount-ra").val($("#slider-ra").slider("values", 0) + " - " +
                        $("#slider-ra").slider("values", 1)); 
    $("#slider-dec").slider({
      orientation: "horizontal",
      range: true,
      min: -90,
      max: 90,
      values: [-90, 90],
      slide: function(event, ui) {
        $("#amount-dec").val(ui.values[0] + " - " + ui.values[1]);
        }
      });
    $("#amount-dec").val($("#slider-dec").slider("values", 0) + " - " +
                         $("#slider-dec").slider("values", 1));
    });
  }
else {
  document.getElementById("hbaseTableSelector").style.display = 'none';
  }
