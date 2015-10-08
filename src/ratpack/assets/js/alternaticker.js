/**
 * Created by noam on 10/8/15.
 */
function updateNumericalStats() {
    $.ajax({
        url: "numericalStats",
        dataType: "json",
        cache: false,
        success: function(data) {
            $('#numericalstats').html("Displaying " + data.scoresSubmitted + " aggregated scores from " + data.countries + " countries.");
        }
    });
}

function updateCountryStats(selected) {
    if (selected == null) {
        $('countryStats').html('');
    } else {
        $.ajax({
            url: "countryFeed/" + selected,
            dataType: "json",
            cache: false,
            success: function(data) {
                var tableData = "<table><tr><th>Test Name</th><th>Average Score</th></tr>";
                for (var testAverageIndex = 0; testAverageIndex < data.length; testAverageIndex++) {
                    var testAverage = data[testAverageIndex];
                    tableData = tableData + "<tr><td>" + testAverage.name + "</td><td>" + testAverage.average + "</td></tr>"
                }
                tableData = tableData + "</table>";
                $('#countryStats').html(tableData);
            }
        });
    }
}