package com.gustavoeliseu.pokedex.ui.network_compose

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.gustavoeliseu.pokedex.R
import com.gustavoeliseu.pokedex.network.connection.ConnectivityObserver
import com.gustavoeliseu.pokedex.network.connection.NetworkConnectivityObserver

@Composable
fun NetworkComposableObserver(){

    val localContext = LocalContext.current
    var currentConnection by remember{
        mutableStateOf( ConnectivityObserver.Status.Available)
    }
    val isConnectedStatus by NetworkConnectivityObserver(localContext).observe().collectAsState(
        initial = ConnectivityObserver.Status.Available
    )
    if(currentConnection != isConnectedStatus){
        val message =  if(isConnectedStatus != ConnectivityObserver.Status.Available) stringResource(
            id = R.string.lost_connection
        ) else stringResource(
            id = R.string.connection_is_back
        )
        Toast.makeText(localContext,message, Toast.LENGTH_SHORT).show()
        currentConnection = isConnectedStatus
    }
}