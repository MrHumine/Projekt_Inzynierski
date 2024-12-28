package com.example.inzynierskiprojekt;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static androidx.test.espresso.intent.Intents.intended;
import android.content.Context;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Rule;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class IntentLoginUITest {

    @Rule
    public ActivityScenarioRule<IntentLogin> activityScenarioRule = new ActivityScenarioRule<IntentLogin>(IntentLogin.class);

    @Before
    public void setUp(){
        Intents.init();
    }
    @After
    public void tearDown(){
        Intents.release();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.inzynierskiprojekt", appContext.getPackageName());
    }

    @Test
    public void isErrorAboutEmptyEmailShowUp(){
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.text_view_login)).check(matches(withText("Uzupełnij email")));
    }

    @Test
    public void isErrorAboutEmptyPasswordShowUp(){
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.text_view_password)).check(matches(withText("Uzupełnij brakujące hasło")));
    }

    @Test
    public void userWriteLoginAndPassword(){
        onView(withId(R.id.login_email_edit_text)).perform(typeText("test3@interia.pl")).check(matches(withText("test3@interia.pl")));
        onView(withId(R.id.login_password_edit_text)).perform(typeText("testoweHaslo")).check(matches(withText("testoweHaslo")));
        onView(withId(R.id.login_button)).perform(click());
        //intended(hasComponent(Menu.class.getName()));
//        intended(IntentMatchers.toPackage("com.example.inzynierskiprojekt.Menu"));

    }

    @Test
    public void userOpenSettings(){
        onView(withId(R.id.toolbar_menu)).perform(click());
    }
}
