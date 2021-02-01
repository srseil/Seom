# Seom

This project implements an agent-based simulation for J. McKenzie Alexander's theory *The Structural Evolution of Morality* [(Alexander 2007)](https://doi.org/10.1017/CBO9780511550997).
The model was built as part of a master's thesis in computer science at LMU Munich, supervised by Prof. Dr. François Bry.
The goal of the thesis is to provide a new agent-based model for Alexander's theory, which can systematically combine different games, network topologies and other parameters at will.
Additionally, the project contains a sensitivity analysis using the *one-factor-at-a-time* method (OFAT).
The model is implemented in Java using the [MASON](https://cs.gmu.edu/~eclab/projects/mason/) library.

## Build

The project was developed in IntelliJ IDEA. To build, import the root of the checked out repository into IntelliJ. The main method lies in `seom.Main`.
It is recommended to add `-ea` to the JVM options, in order to enable assertions which are used in multiple places in the code. The project requires JDK 11.

## Run

To run the sensitivity analysis, simply execute the main method of the class `seom.Main`. The results are stored in a new directory `analysis`.

## Dependencies

All dependencies are bundled with the project in the `lib` directory, except for a suitable JDK.

## Results

Pre-computed results of the sensitivity analysis can be found in the directory `results`. The directory also contains plots of the result data.

## Literature

* The Structural Evolution of Morality Revisited (Seil 2021)

  *Master's thesis at LMU Munich about a new agent-based model for Alexander's theory*

* [Does the Structural Evolution of Morality Lead to Moral Relativism? (Seil 2020)](literature/Seil2020.pdf)

  *Bachelor's thesis at LMU Munich, only relevant for quotations in the main thesis*

* [The Structural Evolution of Morality (Alexander 2007)](https://doi.org/10.1017/CBO9780511550997)

  *Alexander's theory about the evolutionary emergence of moral behaviour*
