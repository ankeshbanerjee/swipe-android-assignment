package com.example.swipeassignment.ui.common.shared_components

import android.view.ViewTreeObserver
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


@Composable
fun CustomOutlinedTextInput(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    readOnly: Boolean = false,
    onClick: () -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val view = LocalView.current
    val viewTreeObserver = view.viewTreeObserver
    DisposableEffect(viewTreeObserver) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val isKeyboardOpen = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
            // ... do anything you want here with `isKeyboardOpen`
            if (!isKeyboardOpen) {
                focusManager.clearFocus()
            }
        }

        viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        readOnly = readOnly,
        placeholder = {
            Text(
                placeholder, style = TextStyle(color = MaterialTheme.colorScheme.secondaryContainer)
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        colors = OutlinedTextFieldDefaults.colors().copy(
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
            focusedSuffixColor = MaterialTheme.colorScheme.primary,
            unfocusedSuffixColor = MaterialTheme.colorScheme.primaryContainer
        ),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .clickable { onClick() }
    )
}

@Preview
@Composable
private fun CustomOutlinedTextInputPreview() {
    CustomOutlinedTextInput(
        "Email", "Enter email", "", {}
    )
}