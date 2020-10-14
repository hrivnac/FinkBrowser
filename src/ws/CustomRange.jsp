<div id="hbaseTableSelector" style="width:100%; background-color:#eeeeee;">

  <style>
    #slider-ra, #slider-dec {
      float: left;
      clear: left;
      width: 300px;
      margin: 15px;
      }
    #slider-ra  .ui-slider-range  {background:   #ef2929;}
    #slider-ra  .ui-slider-handle {border-color: #ef2929;}
    #slider-dec .ui-slider-range  {background:   #8ae234;}
    #slider-dec .ui-slider-handle {border-color: #8ae234;}
    </style>
  
  <table style="background-color:#eeeeee">
    <tr><td rowspan="2"><b>Range<br/>Selectors</b></td>
        <td><label for="amount-ra">ra</label></td>
        <td><input type="text" id="amount-ra"  readonly style="border:0; color:#ef2929; font-weight:bold;"><div id="slider-ra" ></div></td></tr>
    <tr><td><label for="amount-dec">dec</label></td>
        <td><input type="text" id="amount-dec" readonly style="border:0; color:#8ae234; font-weight:bold;"><div id="slider-dec"></div></td></tr>
    <tr><td colspan="2"><b>Free-form<br/>Selector</b></td>
        <td><input type="text" id="ffselector" name="ffselector" size="60">&nbsp;(disables Range Selectors)</td></tr>
    </table>

  <input type='button' onclick='searchRanges("<%=url%>")' value='Search Ranges' style="background-color:#dddddd">

  <hr/>
  
  </div>
