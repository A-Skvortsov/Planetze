## Planetze: Carbon Emissions Tracker

## Project Members

- Aqeeb
- Minki
- Adeeb
- Nethanel
- Andrey

## Code Documentation

USER FLOW

  1. user opens app
	2. user is presented with a sign-up/login module
		i. for sign-up:
			a) user is prompted to enter account information
			b) user is denied the ability to create an account with an email for which an account already exists
			c) user must confirm password (password also has some basic restrictions i.e. no spaces)
			d) if account information is valid, user is sent a verification email and taken to the login page where they can login once email is verified
		ii. for login:
			a) if user tries to login before having verified their email, they will be denied login
			b) if user enters invalid credentials, they are prevented from logging in and told that credentials are invalid
			c) if user enters valid credentials, proceed to 3.
	3. upon logging in:
		i. if it is the user's first login, they are taken to the initialization/annual carbon emissions survey
		ii. otherwise, they are taken directly to the app home page, set as Eco Tracker
	4. initalization/annual carbon emissions survey
		i. a bunch of questions are asked, each multiple choice
		ii. "back" button exists so user can go back and redo answers to questions as they do the survey
		iii. certain answers to certain questions will prevent related questions from being asked (i.e. if user answer "no" to "do you have a car?", no further questions about cars will be asked)
		iv. default country and default car values are saved from inputs to this survey
		v. upon completion of the survey, user is taken to survey results page
	5. survey results page. User is presented with:
		i. a total annual emissions bar
		ii. a graph showing the breakdown of their annual emissions by emissions category (transportation, food, housing and consumption)
		iii. a graph showing the comparison between the user's annual emissions and the annual emissions of a country selected from a dropdown list of countries (default country, as specified in 4iv, is set first)
		iv. a "statistics" module showing statistical (percentage-based) comparisons of user's annual emissions to (1) the national average for the selected country and (2) the global target of 2 tons of co2 per year per person (2 tons global target was given on Piazza)
		v. an option to retake the survey at the bottom of the page (button)
		vi. an option to proceed to "home" (Eco Tracker and central navigation area of the app)
	6. Home/central navigation area of the app
		i. user always has a navigation bar on the bottom of the screen from which they can access Eco Tracker, Eco Guage, Eco Balance and Eco Hub
		ii. user always has a top bar (top of the screen) naming the page they are currently on (out of those mentioned previously) and having a settings option in the top right corner
		iii. settings option clicked opens a new page

FEATURE SUMMARY

  sdfs

ASSUMPTIONS

  asdfasd