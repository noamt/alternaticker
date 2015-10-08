/**
 * Created by noam on 10/8/15.
 */
function getNumericalStats() {
    $.ajax({
        url: "numericalStats",
        dataType: "json",
        cache: false,
        success: function(data) {
            //var json = $.parseJSON(data);
            $('#numericalstats').html("Displaying " + data.scoresSubmitted + " scores from " + data.countries + " countries.");
        }
    });
}