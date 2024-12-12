package lisa.maths.topology

import lisa.automation.settheory.SetTheoryTactics.*
import lisa.maths.Quantifiers.*

import lisa.automation.kernel.CommonTactics.Definition

import lisa.maths.topology.Topology.*
import lisa.maths.topology.Instances.*
import lisa.maths.settheory.*
import lisa.maths.settheory.SetTheory.*
import lisa.maths.settheory.SetTheoryBasics.*
import lisa.automation.kernel.CommonTactics.*
import lisa.maths.settheory.functions.Functionals.*
import lisa.automation.settheory.SetTheoryTactics.UniqueComprehension
import lisa.automation.settheory.SetTheoryTactics.TheConditional

object SingletonSet extends lisa.Main {
  import lisa.maths.settheory.SetTheory.*
  // var defs
  private val x, y, z, a, b, c, t, p, f, s = variable
  private val X, T = variable
  private val S, A, B, Y = variable

  val singletonSetsUniquenes = Theorem(
    ∃!(z, ∀(t, in(t, z) <=> exists(x, in(x, S) /\ (t === singleton(x)))))
  ) {
    val implicationProof = have(exists(x, in(x, S) /\ (t === singleton(x))) ==> in(t, union(cartesianProduct(S, S)))) subproof {

      val elementInSingleton = have(in(x, singleton(x))) subproof {
        have(thesis) by Tautology.from(pairAxiom of (x := x, y := x, z := x))
      }

      val singletonInPowerSet = have(in(x, S) |- in(singleton(x), powerSet(S))) subproof {
        assume(in(x, S))
        val introduceY = thenHave((x === y) |- in(y, S)) by Substitution.ApplyRules(x === y)
        val useSingleton = have(in(y, singleton(x)) |- in(y, S)) by Tautology.from(introduceY, singletonHasNoExtraElements)
        thenHave(() |- (!(in(y, singleton(x))), in(y, S))) by RightNot
        thenHave(() |- in(y, singleton(x)) ==> in(y, S)) by Tautology
        val universalY = thenHave(() |- forall(y, in(y, singleton(x)) ==> in(y, S))) by RightForall
        val singletonIsSubset = have(() |- singleton(x) ⊆ S) by Tautology.from(universalY, subsetAxiom of (z := y, y := S, x := singleton(x)))
        have(thesis) by Tautology.from(singletonIsSubset, powerAxiom of (x := singleton(x), y := S))
      }

      val abExist = have((singleton(t) === pair(x, x)) /\ in(x, S) |- ∃(a, ∃(b, (singleton(t) === pair(a, b)) /\ in(a, S) /\ in(b, S)))) subproof {
        have((singleton(t) === pair(x, x)) /\ in(x, S) |- (singleton(t) === pair(x, x)) /\ in(x, S) /\ in(x, S)) by Restate
        thenHave((singleton(t) === pair(x, x)) /\ in(x, S) |- exists(b, (singleton(t) === pair(x, b)) /\ in(x, S) /\ in(b, S))) by RightExists
        thenHave((singleton(t) === pair(x, x)) /\ in(x, S) |- exists(a, exists(b, (singleton(t) === pair(a, b)) /\ in(a, S) /\ in(b, S)))) by RightExists
      }

      val cartesianInstantiated = have(
        in(singleton(t), cartesianProduct(S, S)) <=> (in(singleton(t), powerSet(powerSet(setUnion(S, S))))
          /\ ∃(a, ∃(b, (singleton(t) === pair(a, b)) /\ in(a, S) /\ in(b, S))))
      ) subproof {
        val instance = have(
          (cartesianProduct(x, y) === cartesianProduct(x, y)) <=> ∀(
            t,
            in(t, cartesianProduct(x, y)) <=> (in(t, powerSet(powerSet(setUnion(x, y))))
              /\ ∃(a, ∃(b, (t === pair(a, b)) /\ in(a, x) /\ in(b, y))))
          )
        ) by InstantiateForall(cartesianProduct(x, y))(cartesianProduct.definition)
        thenHave(
          ∀(
            t,
            in(t, cartesianProduct(x, y)) <=> (in(t, powerSet(powerSet(setUnion(x, y))))
              /\ ∃(a, ∃(b, (t === pair(a, b)) /\ in(a, x) /\ in(b, y))))
          )
        ) by Tautology
        thenHave(
          in(singleton(t), cartesianProduct(x, y)) <=> (in(singleton(t), powerSet(powerSet(setUnion(x, y))))
            /\ ∃(a, ∃(b, (singleton(t) === pair(a, b)) /\ in(a, x) /\ in(b, y))))
        ) by InstantiateForall(singleton(t))
        thenHave(
          forall(
            x,
            in(singleton(t), cartesianProduct(x, y)) <=> (in(singleton(t), powerSet(powerSet(setUnion(x, y))))
              /\ ∃(a, ∃(b, (singleton(t) === pair(a, b)) /\ in(a, x) /\ in(b, y))))
          )
        ) by RightForall
        thenHave(
          in(singleton(t), cartesianProduct(S, y)) <=> (in(singleton(t), powerSet(powerSet(setUnion(S, y))))
            /\ ∃(a, ∃(b, (singleton(t) === pair(a, b)) /\ in(a, S) /\ in(b, y))))
        ) by InstantiateForall(S)
        thenHave(
          forall(
            y,
            in(singleton(t), cartesianProduct(S, y)) <=> (in(singleton(t), powerSet(powerSet(setUnion(S, y))))
              /\ ∃(a, ∃(b, (singleton(t) === pair(a, b)) /\ in(a, S) /\ in(b, y))))
          )
        ) by RightForall
        thenHave(
          in(singleton(t), cartesianProduct(S, S)) <=> (in(singleton(t), powerSet(powerSet(setUnion(S, S))))
            /\ ∃(a, ∃(b, (singleton(t) === pair(a, b)) /\ in(a, S) /\ in(b, S))))
        ) by InstantiateForall(S)
      }

      val init = have(in(x, S) |- in(x, S)) by Restate
      val inSS = have(in(x, S) |- in(x, setUnion(S, S))) by Tautology.from(init, setUnionMembership of (z := x, x := S, y := S))
      val step1 = have(in(x, S) |- in(singleton(x), powerSet(setUnion(S, S))) /\ (singleton(singleton(x)) === pair(x, x))) by
        Tautology.from(inSS, singletonInPowerSet of (S := setUnion(S, S)))
      thenHave((in(x, S), t === singleton(x)) |- in(t, powerSet(setUnion(S, S))) /\ (singleton(t) === pair(x, x))) by
        Substitution.ApplyRules(singleton(x) === t)
      val introduceT = thenHave(in(x, S) /\ (t === singleton(x)) |- in(t, powerSet(setUnion(S, S))) /\ (singleton(t) === pair(x, x))) by Tautology
      val step2 = have(in(x, S) /\ (t === singleton(x)) |- in(singleton(t), powerSet(powerSet(setUnion(S, S)))) /\ (singleton(t) === pair(x, x))) by
        Tautology.from(introduceT, singletonInPowerSet of (x := t, S := powerSet(setUnion(S, S))))
      val extendRHS = have(
        in(x, S) /\ (t === singleton(x)) |- in(singleton(t), powerSet(powerSet(setUnion(S, S)))) /\ in(t, singleton(t))
          /\ ((singleton(t) === pair(x, x)) /\ in(x, S))
      ) by Tautology.from(step2, elementInSingleton of (x := t))
      val existAB = have(
        in(x, S) /\ (t === singleton(x)) |- in(singleton(t), powerSet(powerSet(setUnion(S, S)))) /\ in(t, singleton(t))
          /\ ∃(a, ∃(b, (singleton(t) === pair(a, b)) /\ in(a, S) /\ in(b, S)))
      ) by Tautology.from(extendRHS, abExist)
      val cartesian = have(in(x, S) /\ (t === singleton(x)) |- in(singleton(t), cartesianProduct(S, S)) /\ in(t, singleton(t))) by
        Tautology.from(existAB, cartesianInstantiated)
      val introduceZ = thenHave(in(x, S) /\ (t === singleton(x)) |- exists(z, in(z, cartesianProduct(S, S)) /\ in(t, z))) by RightExists
      val unionOfProduct = have((in(x, S) /\ (t === singleton(x))) |- in(t, union(cartesianProduct(S, S)))) by
        Tautology.from(introduceZ, unionAxiom of (y := z, x := cartesianProduct(S, S), z := t))
      thenHave(exists(x, (in(x, S) /\ (t === singleton(x)))) |- in(t, union(cartesianProduct(S, S)))) by LeftExists
    }
    have(() |- existsOne(z, forall(t, in(t, z) <=> exists(x, in(x, S) /\ (t === singleton(x)))))) by UniqueComprehension.fromOriginalSet(
      union(cartesianProduct(S, S)),
      lambda(t, exists(x, in(x, S) /\ (t === singleton(x)))),
      implicationProof
    )
  }
  val singletonSets = DEF(S) --> The(z, ∀(t, in(t, z) <=> exists(x, in(x, S) /\ (t === singleton(x)))))(singletonSetsUniquenes)

  val singletonSetsMembershipRaw = Theorem(
    in(t, singletonSets(S)) <=> exists(x, ((t === singleton(x)) /\ in(x, S)))
  ) {
    have(∀(t, in(t, singletonSets(S)) <=> exists(x, in(x, S) /\ (t === singleton(x))))) by InstantiateForall(singletonSets(S))(singletonSets.definition)
    thenHave(thesis) by InstantiateForall(t)
  }

  val singletonSetsMembership = Theorem(
    in(x, S) <=> in(singleton(x), singletonSets(S))
  ) {
    val form = formulaVariable
    val memb = have(in(t, singletonSets(S)) <=> exists(x, ((t === singleton(x)) /\ in(x, S)))) by Tautology.from(singletonSetsMembershipRaw)
    have(in(x, S) |- in(singleton(x), singletonSets(S))) subproof {
      assume(in(x, S))
      have(t === singleton(x) |- ((t === singleton(x)) /\ in(x, S))) by Tautology
      thenHave(t === singleton(x) |- exists(x, ((t === singleton(x)) /\ in(x, S)))) by RightExists
      have((t === singleton(x)) ==> in(t, singletonSets(S))) by Tautology.from(lastStep, memb)
      thenHave(forall(t, (t === singleton(x)) ==> in(t, singletonSets(S)))) by RightForall
      thenHave((singleton(x) === singleton(x)) ==> in(singleton(x), singletonSets(S))) by InstantiateForall(singleton(x))
      have(thesis) by Tautology.from(lastStep)
    }
    have(in(singleton(x), singletonSets(S)) |- in(x, S)) subproof {
      assume(in(singleton(x), singletonSets(S)))
      have(() |- exists(y, ((singleton(x) === singleton(y)) /\ in(y, S)))) by Tautology.from(singletonSetsMembershipRaw of (t := singleton(x), y := x))
      thenHave(((singleton(x) === singleton(y)) <=> (x === y)) |- exists(y, (x === y) /\ in(y, S))) by RightSubstIff
        .withParametersSimple(
          List(((singleton(x) === singleton(y)), (x === y))),
          lambda(form, (exists(y, form /\ in(y, S))))
        )

      // thenHave(exists(y, (x === y) /\ in(y, S))) by Restate(singletonExtensionality)
      /*
      val removeExists = have((exists(y, in(y, S) /\ (t === singleton(y))), t === singleton(x)) |- in(x, S)) subproof {
        have((in(y, S), t === singleton(x), t === singleton(y)) |- (in(y, S), t === singleton(x), t === singleton(y)))
        have((in(y, S) /\ (t === singleton(x)) /\ (t === singleton(y))) |- (in(x, S))) by Tautology.from(
          lastStep,
          singletonExtensionality,
          equalityTransitivity of (x := singleton(x), y := t, z := singleton(y)),
          replaceEqualityContainsLeft of (z := S)
        )
        // thenHave(exists(y, in(y, S) /\ (t === singleton(x)) /\ (t === singleton(y))) |- (in(x, S))) by LeftExists
        sorry
        /*have(exists(y, in(y, S) /\ (t === singleton(y))) /\ (t === singleton(x)) |- (in(x, S))) by Tautology.from(
          lastStep,
          existentialConjunctionWithClosedFormula of (x := y, p := forall(z, in(z, z) <=> in(z, z)))
        )
        // Substitution.ApplyRules(existentialConjunctionWithClosedFormula)
        thenHave(thesis) by Tautology*/
      }
      // have((t === singleton(x), in(t, singletonSets(S))) |- (t === singleton(x), exists(x, ((t === singleton(x)) /\ in(x, S))))) by Tautology.from(singletonSetsMembershipRaw of (x := y))
      sorry
      have((t === singleton(x), in(t, singletonSets(S))) |- in(x, S)) by Tautology.from(lastStep, removeExists)
      have(in(singleton(x), singletonSets(S)) |- in(x, S)) by Tautology.from(lastStep, replaceEqualityContainsLeft of (x := t, y := singleton(x), z := singletonSets(S)))
       */
      sorry
    }
    sorry
  }

  val ifContainsSingletonIsDiscrete = Theorem(
    (topology(X, T), ∀(x, x ∈ X ==> singleton(x) ∈ T)) |- discreteTopology(X, T)
  ) {
    assume(∀(x, x ∈ X ==> singleton(x) ∈ T), topology(X, T))
    val topo = have(nonEmpty(X) /\ setOfSubsets(X, T) /\ containsExtremes(X, T) /\ containsUnion(T) /\ containsIntersection(T)) by Tautology.from(topology.definition)
    have(∀(x, x ∈ X ==> singleton(x) ∈ T)) by Tautology
    val singleDef = thenHave((x ∈ X) ==> (singleton(x) ∈ T)) by InstantiateForall(x)
    have(T === powerSet(X)) subproof {
      // show T subs powerSet(X) (by def of topology)
      val left = have(T ⊆ powerSet(X)) by Tautology.from(topo, setOfSubsets.definition)
      // show powerSet(X) subs T

      // For any S ⊆ X we have S = U{x} -> S ∈ T by unionDef
      have((S ⊆ X) |- S ∈ T) subproof {
        assume(S ⊆ X)
        // prove union(cartesianProduct(S, S)) ⊆ T
        // -> S = union(union(cartesianProduct(S, S))) in T
        have(forall(z, in(z, S) ==> in(z, X))) by Tautology.from(subsetAxiom of (x := S, y := X))
        thenHave(in(z, S) ==> in(z, X)) by InstantiateForall(z)
        have(in(z, S) ==> in(singleton(z), T)) by Tautology.from(lastStep, singleDef of (x := z))
        // have(in(z, S) /\ forall(a, in(z, S) <=> in(singleton(z), V)) |- in(singleton(z), V))
        // have(in(z, S) ==> in(singleton(z), T)) by Tautology.from(sorry)
        // have(in(singleton(z), singleton(S)) ==> in(singleton(z), T))
        // have(singleton(S) ⊆ T)
        // have(union(singleton(S)) ∈ T)
        // have(S ∈ T)
        sorry
      }
      have(in(S, powerSet(X)) ==> in(S, T)) by Tautology.from(lastStep, powerAxiom of (x := S, y := X))
      thenHave(forall(S, in(S, powerSet(X)) ==> in(S, T))) by RightForall
      val right = have(powerSet(X) ⊆ T) by Tautology.from(lastStep, subsetAxiom of (x := powerSet(X), y := T, z := S))

      have(thesis) by Tautology.from(left, right, equalityBySubset of (x := powerSet(X), y := T))
    }
    have(discreteTopology(X, T)) by Tautology.from(lastStep, topo, discreteTopology.definition)
  }
}
