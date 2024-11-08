package utilities;

public class Constants {

    //all survey question categories
    public static final String[] categories = {"Transportation", "Food", "Housing", "Consumption"};


    public static final int transport_qs = 7;
    public static final int food_qs = 6;
    public static final int housing_qs = 7;
    public static final int consumption_qs = 4;
    //all survey questions. Each sub array is a question followed by all possible answer options
    public static final String[][] questions = {{"Do you own or regularly use a car?", "Yes", "No"},
            {"What type of car do you drive?", "Gasoline", "Diesel", "Hybrid", "Electric", "I don't know"},
            {"How many kilometers/miles do you drive per year?", "Up to 5,000 km", "5,000–10,000 km", "10,000–15,000 km", "15,000–20,000 km", "20,000–25,000 km", "More than 25,000 km"},
            {"How often do you use public transportation?", "Never", "Occasionally (1-2 times/week)", "Frequently (3-4 times/week)", "Always (5+ times/week)"},
            {"How much time do you spend on public transport per week?", "Under 1 hour", "1-3 hours", "3-5 hours", "5-10 hours", "More than 10 hours"},
            {"How many short-haul flights have you taken in the past year?", "None", "1-2 flights", "3-5 flights", "6-10 flights", "More than 10 flights"},
            {"How many long-haul flights have you taken in the past year?", "None", "1-2 flights", "3-5 flights", "6-10 flights", "More than 10 flights"},
            {"-"},
            {"What best describes your diet?", "Vegetarian", "Vegan", "Pescatarian", "Meat-based"},
            {"How often do you eat beef?", "Daily", "Frequently (3-5 times/week)", "Occasionally (1-2 times/week)", "Never"},
            {"How often do you eat pork?", "Daily", "Frequently (3-5 times/week)", "Occasionally (1-2 times/week)", "Never"},
            {"How often do you eat chicken?", "Daily", "Frequently (3-5 times/week)", "Occasionally (1-2 times/week)", "Never"},
            {"How often do you eat fish?", "Daily", "Frequently (3-5 times/week)", "Occasionally (1-2 times/week)", "Never"},
            {"How often do you waste food or throw away uneaten leftovers?", "Never", "Rarely", "Occasionally", "Frequently"},
            {"-"},
            {"What type of home do you live in?", "Detached house", "Semi-detached house", "Townhouse", "Condo/Apartment", "Other"},
            {"How many people live in your household?", "1", "2", "3-4", "5 or more"},
            {"What is the size of your home?", "Under 1000 sq. ft.", "1000-2000 sq. ft.", "Over 2000 sq. ft."},
            {"What type of energy do you use to heat your home?", "Natural Gas", "Electricity", "Oil", "Propane", "Wood", "Other"},
            {"What is your average monthly electricity bill?", "Under $50", "$50-$100", "$100-$150", "$150-$200", "Over $200"},
            {"What type of energy do you use to heat water?", "Natural Gas", "Electricity", "Oil", "Propane", "Wood", "Other"},
            {"Do you use any renewable energy sources for electricity or heating?", "Yes, primarily", "Yes, partially", "No"},
            {"-"},
            {"How often do you buy new clothes?", "Monthly", "Quarterly", "Annually", "Rarely"},
            {"Do you buy second-hand or eco-friendly products?", "Yes, regularly", "Yes, occasionally", "No"},
            {"How many electronic devices have you purchased in the past year?", "None", "1", "2", "3 or more"},
            {"How often do you recycle?", "Never", "Occasionally", "Frequently", "Always"}};

    public static final double[][][][][] housing_answers = {
            {  //detached house
                    {  //1 person
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }}},
                    {  //2 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }}},
                    {  //3-4 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }}},
                    {  //5 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }}}},
            {  //semi
                    {  //1 person
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }}},
                    {  //2 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }}},
                    {  //3-4 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }}},
                    {  //5 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }}}},
            {  //townhouse/other
                    {  //1 person
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }}},
                    {  //2 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }}},
                    {  //3-4 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }}},
                    {  //5 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }}}},
            {  //condo
                    {  //1 person
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }}},
                    {  //2 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }}},
                    {  //3-4 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }}},
                    {  //5 people
                            {  //<1000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //1-2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }},
                            {  //>2000 sqft
                                    {  //<$50/month bill
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$50-100/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$100-150/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //$150-200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    },
                                    {  //>$200/month
                                            1,  //natural gas
                                            2,  //electricity
                                            3,  //oil
                                            4,  //propane
                                            5  //wood
                                    }}}}};

}
