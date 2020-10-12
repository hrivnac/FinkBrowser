<div id="hbaseTableSelector" style="width:100%; background-color:#eeeeee;">

  <table style="background-color:#eeeeee">
    <tr><td style="padding:15px;" rowspan="2"><b>Range<br/>Selectors</b></td>
        <td style="padding:15px;">ra</td>
        <td style="padding:15px;"><input type="hidden" class="range-slider-ra" name="range-slider-ra"   value="0,180"/></td></tr>
    <tr><td style="padding:15px;">dec</td>
        <td style="padding:15px;"><input type="hidden" class="range-slider-dec" name="range-slider-dec" value="-90,90"/></td></tr>
    <tr><td style="padding:15px;" colspan="2"><b>Free-form<br/>Selector</b></td>
        <td style="padding:15px;"><input type="text" id="ffselector" name="ffselector" size="60">&nbsp;(disables Range Selectors)</td></tr>
    </table>

  <input type='button' onclick='searchRanges("<%=url%>")' value='Search Ranges' style="background-color:#dddddd">

  <hr/>
  </div>
