# PRISONERS DILEMMA

# cooperation incentive

./svg_stack.py --direction=h FullyConnected/cooperation_incentive_morality.svg Lattice1D/cooperation_incentive_morality.svg Lattice2D/cooperation_incentive_morality.svg SmallWorld1D/cooperation_incentive_morality.svg SmallWorld2D/cooperation_incentive_morality.svg BoundedDegree/cooperation_incentive_morality.svg FullyRandom/cooperation_incentive_morality.svg > cooperation_incentive_morality_all.svg

./svg_stack.py --direction=h FullyConnected/cooperation_incentive_stability.svg Lattice1D/cooperation_incentive_stability.svg Lattice2D/cooperation_incentive_stability.svg SmallWorld1D/cooperation_incentive_stability.svg SmallWorld2D/cooperation_incentive_stability.svg BoundedDegree/cooperation_incentive_stability.svg FullyRandom/cooperation_incentive_stability.svg > cooperation_incentive_stability_all.svg

./svg_stack.py --direction=v --margin=50 cooperation_incentive_stability_all.svg cooperation_incentive_morality_all.svg > cooperation_incentive_combined_all.svg


# defection incentive

./svg_stack.py --direction=h FullyConnected/defection_incentive_morality.svg Lattice1D/defection_incentive_morality.svg Lattice2D/defection_incentive_morality.svg SmallWorld1D/defection_incentive_morality.svg SmallWorld2D/defection_incentive_morality.svg BoundedDegree/defection_incentive_morality.svg FullyRandom/defection_incentive_morality.svg > defection_incentive_morality_all.svg

./svg_stack.py --direction=h FullyConnected/defection_incentive_stability.svg Lattice1D/defection_incentive_stability.svg Lattice2D/defection_incentive_stability.svg SmallWorld1D/defection_incentive_stability.svg SmallWorld2D/defection_incentive_stability.svg BoundedDegree/defection_incentive_stability.svg FullyRandom/defection_incentive_stability.svg > defection_incentive_stability_all.svg

./svg_stack.py --direction=v --margin=50 defection_incentive_stability_all.svg defection_incentive_morality_all.svg > defection_incentive_combined_all.svg


# STAG HUNT

# risk dominance

./svg_stack.py --direction=h FullyConnected/risk_dominance_morality.svg Lattice1D/risk_dominance_morality.svg Lattice2D/risk_dominance_morality.svg SmallWorld1D/risk_dominance_morality.svg SmallWorld2D/risk_dominance_morality.svg BoundedDegree/risk_dominance_morality.svg FullyRandom/risk_dominance_morality.svg > risk_dominance_morality_all.svg

./svg_stack.py --direction=h FullyConnected/risk_dominance_stability.svg Lattice1D/risk_dominance_stability.svg Lattice2D/risk_dominance_stability.svg SmallWorld1D/risk_dominance_stability.svg SmallWorld2D/risk_dominance_stability.svg BoundedDegree/risk_dominance_stability.svg FullyRandom/risk_dominance_stability.svg > risk_dominance_stability_all.svg

./svg_stack.py --direction=v --margin=50 risk_dominance_stability_all.svg risk_dominance_morality_all.svg > risk_dominance_combined_all.svg


# GENERAL

# initial moral mean

./svg_stack.py --direction=h FullyConnected/initial_moral_mean_morality.svg Lattice1D/initial_moral_mean_morality.svg Lattice2D/initial_moral_mean_morality.svg SmallWorld1D/initial_moral_mean_morality.svg SmallWorld2D/initial_moral_mean_morality.svg BoundedDegree/initial_moral_mean_morality.svg FullyRandom/initial_moral_mean_morality.svg > initial_moral_mean_morality_all.svg

./svg_stack.py --direction=h FullyConnected/initial_moral_mean_stability.svg Lattice1D/initial_moral_mean_stability.svg Lattice2D/initial_moral_mean_stability.svg SmallWorld1D/initial_moral_mean_stability.svg SmallWorld2D/initial_moral_mean_stability.svg BoundedDegree/initial_moral_mean_stability.svg FullyRandom/initial_moral_mean_stability.svg > initial_moral_mean_stability_all.svg

./svg_stack.py --direction=v --margin=50 initial_moral_mean_stability_all.svg initial_moral_mean_morality_all.svg > initial_moral_mean_combined_all.svg


# learning rule

./svg_stack.py --direction=h FullyConnected/learning_rule_morality.svg Lattice1D/learning_rule_morality.svg Lattice2D/learning_rule_morality.svg SmallWorld1D/learning_rule_morality.svg SmallWorld2D/learning_rule_morality.svg BoundedDegree/learning_rule_morality.svg FullyRandom/learning_rule_morality.svg > learning_rule_morality_all.svg

./svg_stack.py --direction=h FullyConnected/learning_rule_stability.svg Lattice1D/learning_rule_stability.svg Lattice2D/learning_rule_stability.svg SmallWorld1D/learning_rule_stability.svg SmallWorld2D/learning_rule_stability.svg BoundedDegree/learning_rule_stability.svg FullyRandom/learning_rule_stability.svg > learning_rule_stability_all.svg

./svg_stack.py --direction=v --margin=50 learning_rule_stability_all.svg learning_rule_morality_all.svg > learning_rule_combined_all.svg


# mutation probability

./svg_stack.py --direction=h FullyConnected/mutation_probability_morality.svg Lattice1D/mutation_probability_morality.svg Lattice2D/mutation_probability_morality.svg SmallWorld1D/mutation_probability_morality.svg SmallWorld2D/mutation_probability_morality.svg BoundedDegree/mutation_probability_morality.svg FullyRandom/mutation_probability_morality.svg > mutation_probability_morality_all.svg

./svg_stack.py --direction=h FullyConnected/mutation_probability_stability.svg Lattice1D/mutation_probability_stability.svg Lattice2D/mutation_probability_stability.svg SmallWorld1D/mutation_probability_stability.svg SmallWorld2D/mutation_probability_stability.svg BoundedDegree/mutation_probability_stability.svg FullyRandom/mutation_probability_stability.svg > mutation_probability_stability_all.svg

./svg_stack.py --direction=v --margin=50 mutation_probability_stability_all.svg mutation_probability_morality_all.svg > mutation_probability_combined_all.svg


# population size

./svg_stack.py --direction=h FullyConnected/population_size_morality.svg Lattice1D/population_size_morality.svg Lattice2D/population_size_morality.svg SmallWorld1D/population_size_morality.svg SmallWorld2D/population_size_morality.svg BoundedDegree/population_size_morality.svg FullyRandom/population_size_morality.svg > population_size_morality_all.svg

./svg_stack.py --direction=h FullyConnected/population_size_stability.svg Lattice1D/population_size_stability.svg Lattice2D/population_size_stability.svg SmallWorld1D/population_size_stability.svg SmallWorld2D/population_size_stability.svg BoundedDegree/population_size_stability.svg FullyRandom/population_size_stability.svg > population_size_stability_all.svg

./svg_stack.py --direction=v --margin=50 population_size_stability_all.svg population_size_morality_all.svg > population_size_combined_all.svg


# WITHOUT FULLY CONNECTED

# learning distance

./svg_stack.py --direction=h Lattice1D/learning_distance_morality.svg Lattice2D/learning_distance_morality.svg SmallWorld1D/learning_distance_morality.svg SmallWorld2D/learning_distance_morality.svg BoundedDegree/learning_distance_morality.svg FullyRandom/learning_distance_morality.svg > learning_distance_morality_all.svg

./svg_stack.py --direction=h Lattice1D/learning_distance_stability.svg Lattice2D/learning_distance_stability.svg SmallWorld1D/learning_distance_stability.svg SmallWorld2D/learning_distance_stability.svg BoundedDegree/learning_distance_stability.svg FullyRandom/learning_distance_stability.svg > learning_distance_stability_all.svg

./svg_stack.py --direction=v --margin=50 learning_distance_stability_all.svg learning_distance_morality_all.svg > learning_distance_combined_all.svg

# mutation distance

./svg_stack.py --direction=h Lattice1D/mutation_distance_morality.svg Lattice2D/mutation_distance_morality.svg SmallWorld1D/mutation_distance_morality.svg SmallWorld2D/mutation_distance_morality.svg BoundedDegree/mutation_distance_morality.svg FullyRandom/mutation_distance_morality.svg > mutation_distance_morality_all.svg

./svg_stack.py --direction=h Lattice1D/mutation_distance_stability.svg Lattice2D/mutation_distance_stability.svg SmallWorld1D/mutation_distance_stability.svg SmallWorld2D/mutation_distance_stability.svg BoundedDegree/mutation_distance_stability.svg FullyRandom/mutation_distance_stability.svg > mutation_distance_stability_all.svg

./svg_stack.py --direction=v --margin=50 mutation_distance_stability_all.svg mutation_distance_morality_all.svg > mutation_distance_combined_all.svg
