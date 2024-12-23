package daniel.bertoldi.geographyquiz

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import daniel.bertoldi.geographyquiz.fakemodules.FAKE_DATA_STORE_FILE_PREFIX
import daniel.bertoldi.geographyquiz.presentation.navigation.GeographyQuizNavHost
import daniel.bertoldi.geographyquiz.presentation.navigation.ScreenRoutes
import daniel.bertoldi.geographyquiz.presentation.ui.components.ACTION_BUTTON_TEST_TAG
import daniel.bertoldi.geographyquiz.presentation.ui.components.COUNTRY_OPTION_TEST_TAG
import daniel.bertoldi.geographyquiz.presentation.ui.components.DIALOG_CONFIRM_TEST_TAG
import daniel.bertoldi.geographyquiz.presentation.ui.components.GAME_OPTION_TEST_TAG
import daniel.bertoldi.geographyquiz.presentation.ui.components.HOME_SCREEN_TEST_TAG
import daniel.bertoldi.geographyquiz.presentation.ui.components.NEXT_FLAG_TEST_TAG
import daniel.bertoldi.geographyquiz.presentation.ui.components.PLAY_BUTTON_TEST_TAG
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
@HiltAndroidTest
class NavigationHostTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    private lateinit var navController: TestNavHostController

    @Before
    fun setupAppNavHost() {
        composeTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            GeographyQuizNavHost(navigationController = navController)
        }
    }

    @After
    fun teardown() {
        InstrumentationRegistry.getInstrumentation().targetContext.run {
            File(filesDir, "datastore").listFiles()?.forEach {
                if (it.name.contains(FAKE_DATA_STORE_FILE_PREFIX)) {
                    it.delete()
                }
            }
        }
    }

    @Test
    fun geographyQuizNavHost_verifyStartDestination() {
        composeTestRule.onRoot(useUnmergedTree = false).printToLog("layout")
        composeTestRule
            .onNodeWithTag(HOME_SCREEN_TEST_TAG)
            .assertIsDisplayed()
    }

    @Test
    fun geographyQuizNavHost_clickNavigateToRegion_navigateToRegionSelection() {
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(PLAY_BUTTON_TEST_TAG))

        composeTestRule
            .onNodeWithTag(PLAY_BUTTON_TEST_TAG)
            .performClick()

        assertTrue(
            navController
                .currentBackStackEntry!!
                .destination
                .hasRoute<ScreenRoutes.RegionSelection>()
        )
    }

    @Test
    fun geographyQuizNavHost_clickNavigateToArea_navigateToAreaSelection() {
        // Home Screen
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(PLAY_BUTTON_TEST_TAG))
        composeTestRule
            .onNodeWithTag(PLAY_BUTTON_TEST_TAG)
            .performClick()

        // Region Selection Screen
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(GAME_OPTION_TEST_TAG))
        composeTestRule
            .onAllNodesWithTag(GAME_OPTION_TEST_TAG)
            .onFirst()
            .performClick()

        assertTrue(
            navController
                .currentBackStackEntry!!
                .destination
                .hasRoute<ScreenRoutes.AreaSelection>()
        )
    }

    @Test
    fun geographyQuizNavHost_clickNavigateToGameMode_navigateToGameModeSelection() {
        // Home Screen
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(PLAY_BUTTON_TEST_TAG))
        composeTestRule
            .onNodeWithTag(PLAY_BUTTON_TEST_TAG)
            .performClick()

        // Region Selection Screen
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(GAME_OPTION_TEST_TAG))
        composeTestRule
            .onAllNodesWithTag(GAME_OPTION_TEST_TAG)
            .onFirst()
            .performClick()

        // Area Selection Screen
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(GAME_OPTION_TEST_TAG))
        composeTestRule
            .onAllNodesWithTag(GAME_OPTION_TEST_TAG)
            .onFirst()
            .performClick()

        assertTrue(
            navController
                .currentBackStackEntry!!
                .destination
                .hasRoute<ScreenRoutes.GameModeSelection>()
        )
    }

    @Test
    fun geographyQuizNavHost_clickNavigateToFlagGameMode_navigateToFlagGame() {
        // Home Screen
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(PLAY_BUTTON_TEST_TAG))
        composeTestRule
            .onNodeWithTag(PLAY_BUTTON_TEST_TAG)
            .performClick()

        // Region Selection Screen
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(GAME_OPTION_TEST_TAG))
        composeTestRule
            .onAllNodesWithTag(GAME_OPTION_TEST_TAG)
            .onFirst()
            .performClick()

        // Area Selection Screen
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(GAME_OPTION_TEST_TAG))
        composeTestRule
            .onAllNodesWithTag(GAME_OPTION_TEST_TAG)
            .onFirst()
            .performClick()

        // Game Mode Screen
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(GAME_OPTION_TEST_TAG))
        composeTestRule
            .onAllNodesWithTag(GAME_OPTION_TEST_TAG)
            .onFirst()
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(ACTION_BUTTON_TEST_TAG))
        composeTestRule
            .onAllNodesWithTag(ACTION_BUTTON_TEST_TAG)
            .onFirst()
            .performClick()

        assertTrue(
            navController
                .currentBackStackEntry!!
                .destination
                .hasRoute<ScreenRoutes.FlagGame>()
        )
    }

    @Test
    fun geographyQuizNavHost_onBackPressInFlagGameScreen_navigateToHome() {
        // Home Screen
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(PLAY_BUTTON_TEST_TAG))
        composeTestRule
            .onNodeWithTag(PLAY_BUTTON_TEST_TAG)
            .performClick()

        // Region Selection Screen
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(GAME_OPTION_TEST_TAG))
        composeTestRule
            .onAllNodesWithTag(GAME_OPTION_TEST_TAG)
            .onFirst()
            .performClick()

        // Area Selection Screen
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(GAME_OPTION_TEST_TAG))
        composeTestRule
            .onAllNodesWithTag(GAME_OPTION_TEST_TAG)
            .onFirst()
            .performClick()

        // Game Mode Screen
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(GAME_OPTION_TEST_TAG))
        composeTestRule
            .onAllNodesWithTag(GAME_OPTION_TEST_TAG)
            .onFirst()
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(ACTION_BUTTON_TEST_TAG))
        composeTestRule
            .onAllNodesWithTag(ACTION_BUTTON_TEST_TAG)
            .onFirst()
            .performClick()

        // Flag Game Screen
        Espresso.pressBack()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(DIALOG_CONFIRM_TEST_TAG), 30_000L)
        composeTestRule.onNodeWithTag(DIALOG_CONFIRM_TEST_TAG).performClick()

        assertTrue(
            navController
                .currentBackStackEntry!!
                .destination
                .hasRoute<ScreenRoutes.Home>()
        )
    }

    @Test
    fun geographyQuizNavHost_onEndGameScreenPlayAgainClicked_navigateToRegionSelection() {
        // Home Screen
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(PLAY_BUTTON_TEST_TAG))
        composeTestRule
            .onNodeWithTag(PLAY_BUTTON_TEST_TAG)
            .performClick()

        // Region Selection Screen
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(GAME_OPTION_TEST_TAG))
        composeTestRule
            .onAllNodesWithTag(GAME_OPTION_TEST_TAG)
            .onFirst()
            .performClick()

        // Area Selection Screen
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(GAME_OPTION_TEST_TAG))
        composeTestRule
            .onAllNodesWithTag(GAME_OPTION_TEST_TAG)
            .onFirst()
            .performClick()

        // Game Mode Screen
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(GAME_OPTION_TEST_TAG))
        composeTestRule
            .onAllNodesWithTag(GAME_OPTION_TEST_TAG)
            .onFirst()
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(ACTION_BUTTON_TEST_TAG))
        composeTestRule
            .onAllNodesWithTag(ACTION_BUTTON_TEST_TAG)
            .onFirst()
            .performClick()

        // Flag Game Screen
        // Playing game...
        // TODO: sometimes there are 10 elements, others 5 or 20...
        //  Creating a FakeDataSourcesModule didn't work. Should retry later,
        //  'cause I'm tired of trying to make these tests work.
        repeat(20) {
            composeTestRule.waitUntilAtLeastOneExists(hasTestTag(COUNTRY_OPTION_TEST_TAG))
            composeTestRule
                .onAllNodesWithTag(COUNTRY_OPTION_TEST_TAG)
                .onFirst()
                .performClick()
            composeTestRule.waitUntilAtLeastOneExists(hasTestTag(NEXT_FLAG_TEST_TAG))
            composeTestRule.onNodeWithTag(NEXT_FLAG_TEST_TAG).performClick()
        }

        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(ACTION_BUTTON_TEST_TAG))
        composeTestRule.onAllNodesWithTag(ACTION_BUTTON_TEST_TAG).onFirst().performClick()

        assertTrue(
            navController
                .currentBackStackEntry!!
                .destination
                .hasRoute<ScreenRoutes.RegionSelection>()
        )
    }
}