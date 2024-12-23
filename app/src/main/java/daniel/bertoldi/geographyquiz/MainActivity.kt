package daniel.bertoldi.geographyquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import daniel.bertoldi.geographyquiz.presentation.navigation.GeographyQuizNavHost
import daniel.bertoldi.geographyquiz.presentation.ui.theme.GeographyQuizTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GeographyQuizTheme {
                val navigationController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GeographyQuizNavHost(navigationController)
                }
            }
        }
    }
}
