// Chapter 6, Programming Challenge 13
// Population
// Author: David Grant
#include <iostream>
#include <iomanip>
using namespace std;

double population(double pop, double birthRate, double deathRate);
void printPopulations(
  double startPop, double birthRate, double deathRate, int numYears);


	// --------------------------------
	// ----- ENTER YOUR CODE HERE -----
	// --------------------------------
void printPopulations(double startPop, double birthRate, double deathRate, int numYears)
{
	double population = startPop;
	for (int i = 0; i < numYears; i++)
	{
		cout << "Population after year " << i + 1 << ": ";
		population = population + birthRate * population - deathRate * population;
		cout << population << endl;
	}
}

	// --------------------------------
	// --------- END USER CODE --------
	// --------------------------------


int main()
{
    double startPop,        // To hold the starting population.
          birthRate,        // To hold the birth rate.
          deathRate;        // To hold the death rate.
    int   numYears;         // To hold the number of years to track population changes.

    // Input and validate starting population
    cout << "This program calculates population change.\n";
    cout << "Enter the starting population size: ";
    cin  >> startPop;
    while (startPop < 2.0)
    {
        cout << "Starting population must be 2 or more.  Please re-enter: ";
        cin  >> startPop;
    }

    // Input and validate annual birth and death rates
    cout << "Enter the annual birth rate (as % of current population): ";
    cin  >> birthRate;
    while (birthRate < 0)
    {
        cout << "Birth rate percent cannot be negative.  Please re-enter: ";
        cin  >> birthRate;
    }

    birthRate = birthRate / 100;     // Convert from % to decimal.

    cout << "Enter the annual death rate (as % of current population): ";
    cin  >> deathRate;
    while (deathRate < 0)
    {
        cout << "Death rate percent cannot be negative.  Please re-enter: ";
        cin  >> deathRate;
    }

    deathRate = deathRate / 100;     // Convert from % to decimal.

    cout << "For how many years do you wish to view population changes? ";
    cin  >> numYears;
    while (numYears < 1)
    {
        cout << "Years must be one or more. Please re-enter: ";
        cin  >> numYears;
    }

    printPopulations(startPop, birthRate, deathRate, numYears);
    return 0;
}   // End of main function.

