**MACHINE LEARNING**

**The Steps**

1. Find the Dataset
  
   For the dataset we need, we use the dataset on Kaggle with tourism_with_id.csv, tourism_rating.csv, and user.csv.
   The Dataset of tourism_with_id.csv consists of Place_Id, Place_Name, Description, Category, City, Price, Rating, Time_Minutes, Coordinate, Latitude, and Longitude.
   The Dataset of tourism_rating consists of User_Id, Place_Id, and Place_Ratings.
   The Dataset of user.csv consists of User_Id, Location, and Age.
   
2. Preprocessing the Data
  
   Before we process the data, we do something called pre-processing, which is the stage where we change the raw data into data that is ready to be processed, such as cleaning data or removing missing values, normalizing data, etc.
   In this process, there are 3 columns that are removed, that are: Time_Minutes, Coordinate, and Location.
   
3. Make a model

   We create a model based on the desired output requirements, here we use content-based filtering and collaborative filtering to provide recommendations for tourist attractions to our application users. This model is chosen in order we can adjust what the user want such as the their age, category place, price, and rating place.

**Content-Based Filtering**

Content-based filtering is a recommendation system technique that focuses on analyzing the attributes or features of items to suggest similar items to users based on their preferences. Unlike collaborative filtering, content-based filtering does not rely on the behavior or preferences of other users.

**Collaborative Filtering**

Collaborative filtering is a technique used in recommendation systems to predict a user's preferences or interests for a particular item based on information from a group of users with similar preferences. This method relies on analyzing patterns of behavior among users to make recommendations.


From the machine learning model that has been built using embedding layers and regularizers, along with the Adam optimizer, mean absolute error loss function, and MAE (Mean Absolute Error) metric, the testing results of the tourist recommendation system using collaborative filtering approach were obtained.

**Mean Absolute Error**

Mean Absolute Error (MAE) is a commonly used evaluation metric in statistics and machine learning to measure how close, on average, the absolute differences between predicted values by a model and the actual values are.
Mathematically, MAE is calculated by summing the absolute differences between each prediction and the actual value, then dividing it by the total number of samples or data points.


From the recommendation system with collaborative filtering, the MAE value results obtained is 0.24 and the validation loss is 0.33
​
