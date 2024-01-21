import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PlaceAutocompleteAdapter(
    context: Context,
    private val placesClient: PlacesClient
) : ArrayAdapter<AutocompletePrediction>(context, android.R.layout.simple_dropdown_item_1line) {

    private val filter: PlaceAutoCompleteFilter by lazy { PlaceAutoCompleteFilter(placesClient, this) }

    override fun getFilter(): Filter {
        return filter
    }
}

class PlaceAutoCompleteFilter(
    private val placesClient: PlacesClient,
    private val adapter: PlaceAutocompleteAdapter
) : Filter() {

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        val results = FilterResults()

        if (!constraint.isNullOrBlank()) {
            getAutocompletePredictions(constraint.toString()) { predictions ->
                results.values = predictions
                results.count = predictions.size
                adapter.notifyDataSetChanged()
            }
        }

        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        // No need to implement this part
    }

    private fun getAutocompletePredictions(query: String, callback: (List<AutocompletePrediction>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val request = FindAutocompletePredictionsRequest.builder()
                    .setSessionToken(AutocompleteSessionToken.newInstance())
                    .setQuery(query)
                    .build()

                val response = placesClient.findAutocompletePredictions(request).await()
                val predictions = response.autocompletePredictions
                callback(predictions)
            } catch (e: Exception) {
                callback(emptyList())
            }
        }
    }
}
