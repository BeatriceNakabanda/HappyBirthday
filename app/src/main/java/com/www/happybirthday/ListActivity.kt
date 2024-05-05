package com.www.happybirthday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.www.happybirthday.ui.theme.HappyBirthdayTheme

class ListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HappyBirthdayTheme {
                // A surface container using the 'background' color from the theme
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ){
        CardContent(name)
    }
}

@Composable
private fun CardContent(name: String){
    //To preserve state across recompositions, use keyword "remember". It's used to guard against
    // recomposition so that the state is not reset
//    var expanded = remember {
//        mutableStateOf(false)
//    }
    var expanded by rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Text(text = "Hello ")
//                Text(text = name, style = MaterialTheme.typography.headlineMedium)
            Text(
                text = name, style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            if (expanded) {
                Text(
                    text = ("Composem ipsum color sit lazy, " +
                            "padding theme elit, sed do bouncy. ").repeat(4),
                )
            }
        }
        IconButton(onClick = {expanded = !expanded}) {
                Icon(
                    imageVector = if (expanded) Filled.ExpandLess else Filled.ExpandMore,
                    contentDescription = if (expanded) {
                        stringResource(R.string.show_less)
                    } else {
                        stringResource(R.string.show_more)
                    }
                )
            }
    }
//    val extraPadding = if (expanded.value) 48.dp else 0.dp
//    val extraPadding by animateDpAsState(if (expanded.value) 48.dp else 0.dp,
//        animationSpec = spring( //or tween/repeatable
//            dampingRatio = Spring.DampingRatioMediumBouncy,
//            stiffness = Spring.StiffnessLow
//        ))
//    Surface(
//        color = MaterialTheme.colorScheme.primary,
//        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
//    ) {
//        Row(modifier = Modifier.padding(24.dp)){
////            Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
//            Column(
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(bottom = extraPadding.coerceAtLeast(0.dp))
//            ) {
//                Text(text = "Hello ")
////                Text(text = name, style = MaterialTheme.typography.headlineMedium)
//                Text(text = name, style = MaterialTheme.typography.headlineMedium.copy(
//                    fontWeight = FontWeight.ExtraBold
//                ))
//            }
//            ElevatedButton(
//                onClick = { expanded.value = !expanded.value },
//            ){
//                Text(if (expanded.value) "Show less" else "Show more")
//            }
//        }
//    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier){
    //Hoist state => state that is read or modified by multiple functions living in a common ancestor
    //Use by instead of =
    //use rememberSaveable instead of remember to save each state surviving configuration changs such as rotations and process death
    var shouldShowOnboarding by rememberSaveable {
        mutableStateOf(true)
    }
    Surface(modifier){
        if (shouldShowOnboarding){
            OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
        } else {
            Greetings()
        }
    }

}

@Composable
//private fun Greetings(modifier: Modifier = Modifier, names: List<String> = listOf("Gabe", "Ariel")){
private fun Greetings(
    modifier: Modifier = Modifier,
    names: List<String> = List(20) {"$it"}){
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ){
//        Column(modifier = modifier.padding(vertical = 4.dp)){
//            for (name in names){
//                Greeting(name = name)
//            }
//        }
        //LazyColumn reders only the visible items on the screen, allowing performance gains when rendering a big list
        //Lazy column API provides an items element within its scope 
        //LazyColumn and LazyRow == RecyclerView in AndroidViews
        //LazyColumn doesn't recycle its children like RecyclerView. It emits new Composables as you scroll through it and is
//        still performant as emiting Composables is relatively cheap compared to instantiating Android views
        LazyColumn(modifier = modifier.padding(vertical = 4.dp)){
            items(items = names){ name ->
                Greeting(name = name)
            }
        }
    }
}

@Composable
fun OnboardingScreen(
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onContinueClicked
        ){
            Text("Continue")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    HappyBirthdayTheme {
//        MyApp(Modifier.fillMaxSize())
//        OnboardingScreen(onContinueClicked = { /*TODO*/ })
        Greetings()
    }
}
@Preview
@Composable
fun MyAppPreview(){
    HappyBirthdayTheme {
        MyApp(Modifier.fillMaxSize())
    }
}