package com.eunhye.composeplayground.webBrowser


import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WebBrowserScreen() {
    val viewModel = viewModel<WebBrowserViewModel>()
    HomeScreen(viewModel = viewModel)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: WebBrowserViewModel) {
    val focusManager = LocalFocusManager.current
    val (inputUrl, setUrl) = rememberSaveable {
        mutableStateOf("https://www.google.com")
    }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("나만의 웹 브라우저") },
                actions = {
                    IconButton(onClick = { viewModel.undo() } ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back",
                        )
                    }
                    IconButton(onClick = { viewModel.redo() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "forward",
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(20.dp)
                .fillMaxSize(),
        ) {
            OutlinedTextField(
                value = inputUrl,
                onValueChange = setUrl,
                label = { Text(text = "https://") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.url.value = inputUrl
                    focusManager.clearFocus()
                }),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(10.dp ))
            MyWebView(viewModel, snackbarHostState)
        }
    }
}

@Composable
fun MyWebView(viewModel: WebBrowserViewModel, snackbarHostState: SnackbarHostState) {
    val webView = rememberWebView()

    LaunchedEffect(Unit) {
        viewModel.undoSharedFlowv.collectLatest {
            if(webView.canGoBack()) {
                webView.goBack()
            } else {
                snackbarHostState.showSnackbar("더이상 뒤로 갈 수 없습니다.")
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.redoSharedFlowv.collectLatest {
            if(webView.canGoForward()) {
                webView.goForward()
            } else {
                snackbarHostState.showSnackbar("더이상 앞으로 갈 수 없습니다.")
            }
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { webView },
        update = {
            webView.loadUrl(viewModel.url.value)
        }
    )
}

@Composable
fun rememberWebView() : WebView {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl("https://www.google.com")
        }
    }
    return webView
}
