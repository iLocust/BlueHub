package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.db.DatabaseHelper
import com.example.myapplication.db.User
import kotlinx.coroutines.launch

@Composable
fun RegisterPage(navController: NavController, databaseHelper: DatabaseHelper) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val showError = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Register", style = MaterialTheme.typography.h4)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username.value,
                onValueChange = { newValue -> username.value = newValue },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password.value,
                onValueChange = { newValue -> password.value = newValue },
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (username.value.isEmpty() || password.value.isEmpty()) {
                    showError.value = true
                } else {
                    coroutineScope.launch {
                        databaseHelper.userDao().insertUser(User(username = username.value, password = password.value))
                        navController.navigate("login")
                    }
                }
            }) {
                Text("Register")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Already have an account?", textAlign = TextAlign.Center)

            Button(onClick = { navController.navigate("login") }) {
                Text("Login")
            }
        }

        if (showError.value) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 240.dp),
                action = {
                    Button(onClick = { showError.value = false }) {
                        Text("Dismiss")
                    }
                }
            ) {
                Text("Username atau Password tidak boleh kosong sahabat")
            }
        }
    }
}