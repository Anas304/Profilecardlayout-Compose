package com.example.profilecardlayout

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.profilecardlayout.ui.theme.ProfileCardLayoutTheme
import com.example.profilecardlayout.ui.theme.lightGreen
import com.google.ar.core.examples.java.geospatial.GeospatialActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileCardLayoutTheme {
                UserApplication(context = this)
            }

        }
    }
}

@Composable
fun UserApplication(userProfiles: List<UserProfile> = userProfileList, context: Context) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "user_list") {
        composable("user_list") {
            UserListScreen(userProfiles, navController, context)
        }
        composable(
            "user_details_screen/{userId}",
            arguments = listOf(navArgument("userId") {
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
            UserProfileDetailsScreen(navBackStackEntry.arguments!!.getInt("userId"), navController)
        }
    }
}

@Composable
fun UserListScreen(
    userProfiles: List<UserProfile>,
    navController: NavController?,
    context: Context
) {
    Scaffold(topBar = {
        AppBar(
            title = "User List",
            icon = Icons.Default.Home,
            iconClickAction = { }
        )
    }) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Button(onClick = {
                    context.startActivity(Intent(context, GeospatialActivity::class.java))
                }) {
                    Text(text = "Start Goespatial Activity")
                }
            }
           /* LazyColumn() {
                items(userProfiles) { userProfile ->
                    ProfileCard(userProfile = userProfile, onClick = {
                        navController?.navigate("user_details_screen/${userProfile.id}")
                    })
                }
            }*/
        }
    }
}

@Composable
fun AppBar(title: String, icon: ImageVector, iconClickAction: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = icon, "Content Description",
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clickable { iconClickAction.invoke() }
            )
        },
        title = { Text(text = title) }
    )
}

@Composable
fun ProfileCard(userProfile: UserProfile, onClick: () -> Unit) {
    Card(
        shape = CutCornerShape(topEnd = 24.dp),
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .clickable { onClick.invoke() }
            .wrapContentHeight(align = Alignment.Top),
        elevation = 8.dp,
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            ProfilePicture(userProfile.pictureUrl, userProfile.status, 72.dp)
            ProfileContent(userProfile.name, userProfile.status, Alignment.Start)
        }

    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfilePicture(pictureUrl: String, onlineStatus: Boolean, profileImageSize: Dp) {

    Card(
        shape = CircleShape,
        border = BorderStroke(
            width = 2.dp,
            color = if (onlineStatus)
                MaterialTheme.colors.lightGreen else Color.Red
        ),
        modifier = Modifier.padding(16.dp),
        elevation = 8.dp
    ) {
        Image(
            modifier = Modifier.size(profileImageSize),
            painter = rememberImagePainter(data = pictureUrl,
                builder = {
                    /** Empty for now*/
                    transformations(CircleCropTransformation())
                }),
            contentDescription = "Content Description",
        )
    }
}


@Composable
fun ProfileContent(userName: String, onlineStatus: Boolean, alignment: Alignment.Horizontal) {

    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = alignment,
    ) {
        CompositionLocalProvider(
            LocalContentAlpha provides
                    if (onlineStatus) 1f else ContentAlpha.medium
        ) {
            Text(text = userName, style = MaterialTheme.typography.h5)
        }
        //CompositionLocalProvider is used for making the Text Transparent
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = if (onlineStatus)
                    "Active now" else "Offline",
                style = MaterialTheme.typography.body2
            )

        }
    }
}

@Composable
fun UserProfileDetailsScreen(userId: Int, navController: NavController?) {

    val userProfile = userProfileList.first { userProfile -> userId == userProfile.id }

    Scaffold(topBar = {
        AppBar(
            title = "User Details",
            icon = Icons.Default.ArrowBack,
            iconClickAction = { navController?.navigateUp() }
        )
    }) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                ProfilePicture(userProfile.pictureUrl, userProfile.status, 240.dp)
                ProfileContent(userProfile.name, userProfile.status, Alignment.CenterHorizontally)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserDetailsScreenPreview() {
    ProfileCardLayoutTheme {
        UserProfileDetailsScreen(userId = 0, navController = null)
    }
}

//@Preview(showBackground = true)
//@Composable
//fun UserListPreview() {
//    ProfileCardLayoutTheme {
//        UserListScreen(userProfiles = userProfileList, null)
//    }
//}