if (typeof hideHBaseSelector === 'undefined' || !hideHBaseSelector) {
  $(document).ready(function(){
    $('.range-slider-ra').jRange({
      from: 0,
      to: 180,
      step: 1,
      scale: [0,45,90,135,180],
      format: '%s',
      width: 600,
      showLabels: true,
      isRange: true,
      theme: 'theme-blue'
      });
    $('.range-slider-dec').jRange({
      from: -90,
      to: 90,
      step: 1,
      scale: [-90,-45,0,45,90],
      format: '%s',
      width: 600,
      showLabels: true,
      isRange: true,
      theme: 'theme-blue'
      });
    });
  }
else {
  document.getElementById("hbaseTableSelector").style.display = 'none';
  }
