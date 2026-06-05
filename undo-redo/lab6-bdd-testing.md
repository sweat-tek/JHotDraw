# Lab 6 — Behaviour-Driven Testing (User Stories → JGiven Scenarios)

> **Project:** JHotDraw · **Feature / CR:** Undo/Redo
> **Tools:** [JGiven](https://jgiven.org) (Given-When-Then automation) + [AssertJ](https://assertj.github.io/doc/) (fluent assertions)
> **Unit under test:** `UndoRedoManager` — the core domain logic of the feature.

## Introduction — User Stories and BDD

A **user story** is a short description of a capability from the user's perspective:
*"As a [user type], I want [some goal] so that [some reason]."*
In **Behaviour-Driven Development** each story is turned into one or more **Given-When-Then** scenarios that become executable tests.

## Part 1 — User stories mapped to BDD scenarios

| # | User story | BDD scenario (Given-When-Then) |
|---|------------|--------------------------------|
| **US-1** | As a drawing user, I want to **undo** my last change so that I can correct mistakes. | **Given** an undo manager with one recorded edit · **When** the user triggers Undo · **Then** the edit is reversed **and** Redo becomes available. |
| **US-2** | As a drawing user, I want to **redo** a change I undid so that I can reapply it. | **Given** an edit has been recorded and undone · **When** the user triggers Redo · **Then** the edit is reapplied **and** Undo becomes available again. |
| **US-3** | As a drawing user, I want Undo to be **safe when there is nothing to undo** so that the app does not crash. | **Given** a fresh undo manager · **When** the user triggers Undo · **Then** the undo is rejected safely (`CannotUndoException`) and nothing changes. |

## Part 2 — Automating the scenarios with JGiven

JGiven structures each scenario into three **stages** whose step methods read like the Given-When-Then text. State is shared between stages with `@ScenarioState`.

**Test:** `jhotdraw-utils/src/test/java/org/jhotdraw/undo/bdd/UndoRedoBddTest.java`

```java
public class UndoRedoBddTest extends ScenarioTest<GivenAnUndoManager, WhenTheUser, ThenTheOutcome> {

    @Test
    public void user_can_undo_the_last_change() {
        given().an_undo_manager()
                .and().an_edit_has_been_recorded();
        when().the_user_triggers_undo();
        then().the_last_edit_is_reversed()
                .and().redo_becomes_available();
    }
    // … US-2, US-3 …
}
```

The stages:

| Stage | Steps |
|-------|-------|
| `GivenAnUndoManager` | `an_undo_manager()`, `an_edit_has_been_recorded()`, `the_edit_has_been_undone()` |
| `WhenTheUser` | `the_user_triggers_undo()`, `the_user_triggers_redo()`, `the_user_triggers_undo_expecting_failure()` |
| `ThenTheOutcome` | `the_last_edit_is_reversed()`, `redo_becomes_available()`, `the_last_edit_is_reapplied()`, `undo_becomes_available_again()`, `the_undo_is_rejected_safely()` |

## Part 3 — Domain assertions with AssertJ

The `Then` steps use **AssertJ** fluent, self-describing assertions:

```java
assertThat(lastEdit.undoCount).as("edit was undone once").isEqualTo(1);
assertThat(manager.canRedo()).as("redo is now possible").isTrue();
assertThat(caughtUndoError)
        .as("undo on an empty stack is rejected with CannotUndoException")
        .isInstanceOf(CannotUndoException.class);
```

### Why AssertJ-core (not AssertJ-Swing) here

The lab notes **AssertJ-Swing** for driving Swing UIs. That tool automates the GUI via an AWT `Robot` and **needs a display**, so it suits *end-to-end* scenarios that click `Edit → Undo` in the running app (`DrawView`/`Main`). The behaviour worth verifying for this feature lives in the **headless domain object** `UndoRedoManager`, so these scenarios test it directly with **AssertJ-core** — faster, deterministic, and runnable in CI without a display. AssertJ-Swing would be the right tool for a follow-up GUI scenario layer.

## Dependencies added (`jhotdraw-utils/pom.xml`)

```xml
<dependency>
  <groupId>com.tngtech.jgiven</groupId>
  <artifactId>jgiven-junit</artifactId>
  <version>1.3.1</version>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>org.assertj</groupId>
  <artifactId>assertj-core</artifactId>
  <version>3.25.3</version>
  <scope>test</scope>
</dependency>
```

## Verification — results

| Command | Result |
|---------|--------|
| `mvn -pl jhotdraw-utils test` | **BUILD SUCCESS** — `Tests run: 17, Failures: 0, Errors: 0` (3 JGiven scenarios + 10 JUnit + 4 TestNG). |

JGiven also writes a structured report to `target/jgiven-reports/` (JSON; an HTML report can be generated from it), giving living documentation of the scenarios straight from the test run.

## Summary

The Undo/Redo feature's user stories (US-1 undo, US-2 redo, US-3 safe-empty-undo) are expressed as executable **Given-When-Then** scenarios with **JGiven**, asserted with **AssertJ**, and run green alongside the Lab 5 unit tests. Together, Lab 5 (unit) and Lab 6 (behaviour) verify the feature both at the method level and at the user-story level.
