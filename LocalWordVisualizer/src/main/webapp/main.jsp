<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
  <title>LocalWordVisualizer</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.2/css/materialize.min.css">
  <script src="https://code.jquery.com/jquery-3.2.1.min.js"
          integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=" crossorigin="anonymous"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"
          integrity="sha256-VazP97ZCwtekAsvgPBSUwPFKdrwD3unUfSGVYrahUqU=" crossorigin="anonymous"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.2/js/materialize.min.js"></script>
  <script src="https://www.gstatic.com/charts/loader.js"></script>
</head>
<body>

<nav class="blue darken-1">
  <div class="nav-wrapper">
    <a href="#" class="brand-logo right hide-on-med-and-down">LocalWordVisualizer</a>
    <a href="#" data-activates="mobile" class="button-collapse"><i class="material-icons">menu</i></a>
    <ul class="left hide-on-med-and-down">
      <li><a href="#aboutModal"><i class="material-icons left">info_outline</i>About</a></li>
      <li><a href="https://github.com/robertdhx/thesis"><i class="material-icons left">code</i>GitHub</a></li>
    </ul>
    <ul class="side-nav" id="mobile">
      <li><a href="#aboutModal"><i class="material-icons left">info_outline</i>About</a></li>
      <li><a href="https://github.com/robertdhx/thesis"><i class="material-icons left">code</i>GitHub</a></li>
    </ul>
  </div>
</nav>

<div class="container">
  <div class="row">
    <form id="searchForm" method="post" class="col s12">
      <div class="row">
        <div class="input-field col s12">
          <input type="text" id="search" name="query" class="autocomplete" autocomplete="off">
          <label for="search" class="active">Enter a word</label>
        </div>
      </div>
    </form>
  </div>
  <c:if test="${not empty selectedLocalWord}">
    <div class="row">
      <h4>Results for '${selectedLocalWord.value}' (${fn:substring(selectedLocalWord.type, 0, 1)})</h4>
    </div>
    <div class="row">
      <div class="col s12 m12 l6">
        <table>
          <thead>
          <tr>
            <th>Position</th>
            <th>Province</th>
            <th>Probability</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="probability" items="${selectedLocalWord.probabilities}" varStatus="loop">
            <tr>
              <td>${loop.index + 1}</td>
              <td>${probability.province}</td>
              <td><fmt:formatNumber value="${probability.value}" maxFractionDigits="10"/></td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
      <div class="col s12 m12 l6">
        <div id="map">
        </div>
      </div>
    </div>
    <script>
      google.charts.load('current', {'packages': ['geochart']});
      google.charts.setOnLoadCallback(drawRegionsMap);

      function drawRegionsMap() {
        var data = google.visualization.arrayToDataTable([
          ['Province', 'Probability'], <c:forEach var="probability" items="${selectedLocalWord.probabilities}">
          ['NL-${probability.province}', ${probability.value}], </c:forEach>
        ]);

        var options = {
          region: 'NL',
          resolution: 'provinces',
          legend: 'none'
        };

        var chart = new google.visualization.GeoChart(document.getElementById('map'));
        chart.draw(data, options);
      }
    </script>
  </c:if>
</div>

<div id="aboutModal" class="modal">
  <div class="modal-content">
    <h4>About</h4>
    <p>This application is part of my Master thesis about geolocating Dutch Twitter users.</p>
    <h5>How to use?</h5>
    <p>Just enter a word in the search box. Click one of the suggestions to view probabilities.
      The T, D and H indicate the source of the word (tweet, user description or hashtag).</p>
  </div>
  <div class="modal-footer">
    <a href="#!" class="modal-action modal-close waves-effect waves-green btn-flat">Close</a>
  </div>
</div>

<script>
  $(document).ready(function () {
    $('.button-collapse').sideNav();
    $('.modal').modal();

    $('input.autocomplete').autocomplete({
      data: {
        <c:forEach var="localWord" items="${localWordList}">'${localWord.value} (${fn:substring(localWord.type, 0, 1)})': null, </c:forEach>
      },
      onAutocomplete: function (val) {
        $("#searchForm").submit();
      },
      limit: 200
    });
  });
</script>

</body>
</html>