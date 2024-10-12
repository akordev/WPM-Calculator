# WPM design solution

## Notes:
### Out of scope:
- The solution only works for a hardware keyboard. This is done for simplicity.

  #### Few thoughts about implementation for sofkeyboard:
    - What do we do with swipes?
    - Do we allow custom keyboards?
    - Ultimately we'll probably settle on a solution that uses our own implemented keyboard to have control over how/what user can type

- Multi-user support?
- Wrong typed text indication
- Tests

## Design solution overview [solution overview](../docs/Solution_Overview.drawio)

### Thoughts about WPM implementation:
      - Performance. Always write and read from db might be not optimal solution. 
        Consider move calculation using RAM and save data on pauses or when test is finished 
      - Do we want to use it as test? then we need to think how to prevent cheating like putting invivible simbols 
      - Calculation strategy. There are many ways to calculate wpm, I implemented popular aproach when we consider every 5 simbols as word.
### WPM design - [WPM design solution](../docs/WPMCalculator_Design.drawio)
    