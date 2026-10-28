
package com.duoc.agriserv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.duoc.agriserv.ui.theme.AgriServTheme
import com.duoc.agriserv.ui.theme.LoginScreen
import com.duoc.agriserv.ui.theme.Navegacion
import com.duoc.agriserv.ui.theme.SingInScreen
import com.duoc.agriserv.viewModel.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AgriServTheme {

                Navegacion()
            }
        }
    }
}
