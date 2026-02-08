# QuantumInput
#### You only know the inputs state if you observe it




```mermaid
%%{ init: { 'flowchart': { 'curve': 'stepBefore', 'defaultRenderer': 'elk' }, 'layout': 'elk' } }%%
flowchart TB
    A("Platform") e1@-- events --> B("InputProcessor")
    B e2@-- updates --> C("InputState")
    C e3@-- queried by --> D("ActionBindings")
    D e4@-- resolved by --> E("Contexts")
    B --- H[" "]
    C --- H
    D --- H
    E --- H
    H e5@-- exposed via --> I("InputSystem")
    
    e1@{ curve: stepAfter }
    e2@{ curve: stepAfter }
    e3@{ curve: stepAfter }
    e4@{ curve: stepAfter }
    e5@{ curve: stepAfter }

    style H height:0px,width:1px,fill:none,stroke:none,background-color:none
```
<!--
[Platform]
    ↓ events
[InputProcessor]
    ↓ updates
[InputState]
    ↓ queried by
[ActionBindings]
    ↓ resolved by
[Contexts]
    ↓ exposed via
[InputSystem]

All parts are exposed to InputSystem
-->