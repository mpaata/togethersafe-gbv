# DESIGN_SYSTEM.md --- Shared Document

## 1. How to Use This Document
- **JavaFX Developer:** paste alongside DESKTOP.md
- **Backend Developer:** reference this for user-facing messages
- **Never invent colors, fonts, or spacing outside this document.**
- **Never change the design system without team agreement.**
- Every screen in TogetherSafe must follow this system.

## 2. Design Principles
1. **Safety over style** --- every element must reduce stress, never increase it.
2. **Clarity over decoration** --- plain language, no jargon.
3. **Consistency above cleverness** --- same button, same color, same behavior.
4. **Accessible by default** --- WCAG 2.1 AA minimum.
5. **Calm, not clinical** --- soft colors, generous whitespace, no alarming reds except for SOS/errors.
6. **Fast feels safe** --- no long animations, no blocking.
7. **Keyboard-first** --- every action reachable without a mouse.

## 3. Colors

### Primary palette (Calm, trustworthy)
| Name | Hex | Use |
|---|---|---|
| Primary | `#4F46E5` | Primary buttons, active nav, links |
| Primary Hover | `#4338CA` | Button hover |
| Primary Light | `#EEF2FF` | Selected row, active nav bg |

### Semantic colors
| Name | Hex | Use |
|---|---|---|
| Success | `#059669` | Resolved case, success messages |
| Success Light | `#ECFDF5` | Success badge bg |
| Warning | `#F59E0B` | Pending, attention needed |
| Warning Light | `#FFFBEB` | Warning badge bg |
| Danger | `#DC2626` | Errors, delete, SOS |
| Danger Light | `#FEF2F2` | Error badge bg |
| Info | `#2563EB` | Info messages |
| Info Light | `#EFF6FF` | Info badge bg |

### **CRITICAL: SOS / Emergency color**
| Name | Hex | Use |
|---|---|---|
| SOS | `#DC2626` | SOS button only |
| SOS Hover | `#B91C1C` | SOS hover |
| SOS Text | `#FFFFFF` | SOS text |

**The SOS button is the ONLY element on the entire app allowed to use full-saturation red.** This makes it instantly recognizable in a crisis.

### Neutrals
| Name | Hex | Use |
|---|---|---|
| Background | `#F8FAFC` | Page background |
| Surface | `#FFFFFF` | Cards, panels |
| Border | `#E2E8F0` | Dividers, inputs |
| Border Strong | `#CBD5E1` | Emphasized borders |
| Text Primary | `#0F172A` | Body text |
| Text Secondary | `#475569` | Secondary text |
| Text Muted | `#64748B` | Labels, hints |
| Text Disabled | `#94A3B8` | Disabled states |

### Case status colors
| Status | Text | Background |
|---|---|---|
| OPEN | `#1D4ED8` | `#EFF6FF` |
| IN_PROGRESS | `#B45309` | `#FFFBEB` |
| RESOLVED | `#047857` | `#ECFDF5` |
| CLOSED | `#475569` | `#F1F5F9` |

### Priority colors
| Priority | Text | Background |
|---|---|---|
| LOW | `#475569` | `#F1F5F9` |
| MEDIUM | `#1D4ED8` | `#EFF6FF` |
| HIGH | `#B45309` | `#FFFBEB` |
| CRITICAL | `#B91C1C` | `#FEF2F2` |

## 4. Typography

### Font families
| Purpose | Font | Weight range |
|---|---|---|
| UI text | Inter | 400, 500, 600, 700 |
| Numbers, IDs, checksums | JetBrains Mono | 400, 500 |

**Fallback:**
- Desktop: `Inter, "Segoe UI", sans-serif`
- Mono: `JetBrains Mono, "Courier New", monospace`

### Type scale
| Name | Size | Weight | Line height | Use |
|---|---|---|---|---|
| Display | 32px | 700 | 1.2 | Login page title |
| H1 | 24px | 600 | 1.3 | Page titles |
| H2 | 20px | 600 | 1.35 | Section titles |
| H3 | 18px | 600 | 1.4 | Card titles |
| Body | 16px | 400 | 1.5 | Default text |
| Body Small | 14px | 400 | 1.5 | Labels, secondary text |
| Body Small Med | 14px | 500 | 1.5 | Emphasis in labels |
| Tiny | 12px | 500 | 1.4 | Badges, meta |
| Mono | 14px | 500 | 1.4 | Numbers, IDs |

### Rules
- Never use more than 3 weights on one screen.
- Headings always 600, never 700 (except Display).
- Body text never smaller than 14px.
- Case IDs, checksums, and reference numbers always JetBrains Mono.
- Never use ALL CAPS for body text.
- Never use italics for emphasis --- use weight or color.
- Line length: 60--80 characters for body text.

## 5. Spacing
Use a consistent 4px scale everywhere. No eyeballing.

| Token | Pixels | Use |
|---|---|---|
| space-1 | 4px | Micro spacing |
| space-2 | 8px | Small gaps |
| space-3 | 12px | Form gaps |
| space-4 | 16px | Card padding |
| space-5 | 20px | Between sections |
| space-6 | 24px | Card padding (large) |
| space-8 | 32px | Between page sections |
| space-10 | 40px | Page-level spacing |
| space-12 | 48px | Between major blocks |

### Common patterns
- Inside a card: `16px` or `24px`
- Between cards: `gap-4` (16px)
- Between sections: `mb-6` or `space-y-6`
- Between form fields: `space-y-4`
- Between page and content: `py-6 px-4`

**Rule:** If unsure, use the next size up. Whitespace is calm.

## 6. Border Radius
| Element | Radius |
|---|---|
| Inputs | 8px |
| Buttons | 8px |
| Cards | 12px |
| Modals | 16px |
| Badges | Full pill |
| Avatars | Full circle |

**Consistency matters more than exact values.**

## 7. Shadows
Subtle and layered. Never dramatic.

| Name | Use |
|---|---|
| shadow-sm | Cards, inputs |
| shadow-md | Dropdowns, hover cards |
| shadow-lg | Popovers |
| shadow-xl | Modals |

**Rules:**
- Default cards use `shadow-sm`.
- On hover, cards lift to `shadow-md`.
- Modals always use `shadow-xl`.
- Never use heavy drop shadows.

## 8. Components

### Buttons

**Primary** --- the main action on a screen. Only one per screen.
- Background: `#4F46E5`
- Text: White
- Padding: 10px 20px
- Radius: 8px
- Font: 14px, weight 600
- Hover: `#4338CA`
- Focus: 2px ring, primary color
- Disabled: 50% opacity

**Secondary** --- alternative actions.
- Background: White
- Text: `#0F172A`
- Border: 1px solid `#E2E8F0`
- Padding: 10px 20px
- Radius: 8px
- Hover: Background becomes `#F8FAFC`

**Ghost** --- tertiary actions, like "Cancel" in a modal.
- Background: transparent
- Text: `#475569`
- Hover: Background becomes `#F8FAFC`

**Danger** --- destructive actions.
- Background: `#DC2626`
- Text: White
- Hover: `#B91C1C`

**SOS Button** --- emergency only.
- Background: `#DC2626`
- Text: White
- Font size: 18px, weight 700
- Padding: 16px 32px
- Radius: 12px
- Always visible, never hidden behind a menu.
- No confirmation dialog.

**Sizes:**
- sm --- `px-3 py-1.5 text-sm`
- md --- `px-5 py-2.5 text-sm` (default)
- lg --- `px-6 py-3 text-base`

**States:** default, hover, focus, active, disabled, loading.

### Inputs
- Height: 40px (default), 48px (large)
- Padding: 10px 12px
- Border: 1px solid `#E2E8F0`
- Radius: 8px
- Font: 14px, Inter
- Background: White
- Focus: border primary, 2px ring
- Error: border danger, error text below
- Disabled: `#F1F5F9` bg, `#94A3B8` text

**Every input has:**
- A label above (14px, weight 500, text-slate-700)
- Optional help text below (12px, text-slate-500)
- Optional error text below (12px, text-danger)

### Tables
**Used for:** lists of cases, evidence, users, audit log.
- Header: bg-slate-50, weight 600, 12px uppercase tracking-wide
- Rows: 48px height, 1px bottom border
- Hover: bg-slate-50
- Selected: bg-indigo-50
- Numbers/IDs: JetBrains Mono, right-aligned
- Actions: right column, ghost buttons
- Sticky header on long tables

### Cards
- Background: White
- Radius: 12px
- Shadow: shadow-sm
- Padding: 24px

**Anatomy:**
- Optional header: title (H3) + action button
- Body: content
- Optional footer: secondary actions

### Badges
Small pill-shaped labels for state.

| State | Style |
|---|---|
| OPEN | bg-blue-50, text-blue-700 |
| IN_PROGRESS | bg-amber-50, text-amber-700 |
| RESOLVED | bg-emerald-50, text-emerald-700 |
| CLOSED | bg-slate-100, text-slate-700 |
| LOW | bg-slate-100, text-slate-700 |
| MEDIUM | bg-blue-50, text-blue-700 |
| HIGH | bg-amber-50, text-amber-700 |
| CRITICAL | bg-red-50, text-red-700 |

- Padding: 4px 10px
- Radius: full pill
- Font: 12px, weight 600

### Modals
- Backdrop: black at 50% opacity, blurred
- Panel: white, radius 16px, max-width 480px
- Padding: 24px
- Shadow: shadow-xl

**Behavior:**
- Escape closes.
- Clicking backdrop closes (except for crisis modals).
- Focus trapped inside.
- Confirm dialogs for destructive actions.

### Toasts
- Position: top-right (fixed)
- Max width: 360px
- Radius: 8px
- Padding: 12px 16px
- Shadow: shadow-lg
- Animation: slide-in from right, auto-dismiss in 5s

| Type | Style |
|---|---|
| Success | bg-emerald-600, text-white, check icon |
| Error | bg-red-600, text-white, alert icon |
| Info | bg-blue-600, text-white, info icon |
| Warning | bg-amber-500, text-white, warning icon |

### Empty states
Every list has an empty state.
- Icon: 48px, slate-300
- Title: 16px, weight 600, slate-700
- Description: 14px, slate-500
- Action: primary or secondary button, optional
- Padding: py-12
- Center-aligned

**Example:** "No cases yet. [Report a Case]"

### Loading states
- **Skeletons:** for lists --- grey blocks with shimmer.
- **Spinners:** for buttons and short operations.

**Rules:**
- Delay showing spinner by 200ms (avoid flicker).
- Never show a blank screen.
- Never block the whole UI for a small action.

### Icons
- **Library:** Lucide (for both desktop and any future web).
- **Sizes:** 16px (inline), 20px (buttons), 24px (nav), 32px (feature).
- **Rules:**
  - Always pair icons with text (except on mobile bottom nav).
  - Never use icon-only buttons in critical flows.
  - Icons inherit text color.

## 9. Layouts

### Desktop (JavaFX) --- Primary Layout
```text
+----------------------------------------------------------+
| Top bar (56px)                                           |
+-------------+--------------------------------------------+
|             |                                            |
| Sidebar     | Content area                               |
| (240px)     |                                            |
|             |                                            |
+-------------+--------------------------------------------+



Sidebar collapsible to 64px (icon only).

Content max width: 1280px, centered.

Content padding: 24px.

Minimum window: 1280x720.

SOS button always pinned to top-right of the top bar OR always visible as a floating button.

Disguised Mode Layout
When Disguised Mode is active:

Window title becomes neutral (e.g., "System Update").

App is hidden from taskbar.

Branding is removed (no "TogetherSafe" text visible).

Sidebar collapses to a minimal look.

10. Accessibility
Every screen must pass WCAG 2.1 AA.

Color contrast
Body text on white: at least 4.5:1.

Large text (18px+): at least 3:1.

Focus indicators: visible at 3:1.

All the palette colors above meet this.

Keyboard
Every interactive element reachable by Tab.

Focus visible on every focused element (2px ring, primary color).

Escape closes modals and dropdowns.

Enter activates the focused element.

Screen readers
Every input has an associated label.

Every icon-only button has an accessible name.

Meaningful alt text on images.

Forms
Errors described in text, not just color.

Success messages clear and specific.

Required fields marked with *.

Never rely on color alone
Always pair color with text or icon.

Never use hover-only to reveal important info.

11. Motion
Minimal animation. Only where it helps.

Animation	Duration	Easing
Hover lift	150ms	ease-out
Modal enter	200ms	ease-out
Toast slide-in	250ms	ease-out
Button spinner	infinite	linear
Never:

Animate on page load.

Use bounce or spring easing.

Animate more than 3 elements at once.

Animate for more than 300ms.

Prefers-reduced-motion: honour it. Disable all transitions if the user's OS requests it.

12. JavaFX CSS Example (Reference)
css
.root {
    -fx-font-family: "Inter";
    -fx-font-size: 14px;
    -fx-background-color: #F8FAFC;
}

.h1 {
    -fx-font-size: 24px;
    -fx-font-weight: 600;
    -fx-text-fill: #0F172A;
}

.card {
    -fx-background-color: white;
    -fx-background-radius: 12;
    -fx-padding: 24;
    -fx-effect: dropshadow(gaussian, rgba(15,23,42,0.06), 6, 0, 0, 2);
}

.btn-primary {
    -fx-background-color: #4F46E5;
    -fx-text-fill: white;
    -fx-background-radius: 8;
    -fx-padding: 10 20;
    -fx-font-weight: 600;
    -fx-font-size: 14px;
    -fx-cursor: hand;
}

.btn-primary:hover { -fx-background-color: #4338CA; }
.btn-primary:disabled { -fx-opacity: 0.5; }

.btn-sos {
    -fx-background-color: #DC2626;
    -fx-text-fill: white;
    -fx-background-radius: 12;
    -fx-padding: 16 32;
    -fx-font-weight: 700;
    -fx-font-size: 18px;
    -fx-cursor: hand;
}

.btn-sos:hover { -fx-background-color: #B91C1C; }

.badge-open { -fx-background-color: #EFF6FF; -fx-text-fill: #1D4ED8; -fx-background-radius: 999; -fx-padding: 4 10; -fx-font-size: 12px; -fx-font-weight: 600; }
.badge-in-progress { -fx-background-color: #FFFBEB; -fx-text-fill: #B45309; -fx-background-radius: 999; -fx-padding: 4 10; -fx-font-size: 12px; -fx-font-weight: 600; }
.badge-resolved { -fx-background-color: #ECFDF5; -fx-text-fill: #047857; -fx-background-radius: 999; -fx-padding: 4 10; -fx-font-size: 12px; -fx-font-weight: 600; }
.badge-critical { -fx-background-color: #FEF2F2; -fx-text-fill: #B91C1C; -fx-background-radius: 999; -fx-padding: 4 10; -fx-font-size: 12px; -fx-font-weight: 600; }

.input {
    -fx-background-color: white;
    -fx-border-color: #E2E8F0;
    -fx-border-radius: 8;
    -fx-background-radius: 8;
    -fx-padding: 10 12;
    -fx-font-size: 14px;
}

.input:focused {
    -fx-border-color: #4F46E5;
}
13. What NOT to Do
Don't invent colors --- use the palette.

Don't use more than 2 font sizes in a single card.

Don't use shadows heavier than shadow-xl.

Don't use border radius other than the defined set.

Don't use ALL CAPS in body text.

Don't use red for anything except SOS, errors, critical priority, or fail states.

Don't animate on page load.

Don't use icon-only buttons in critical flows.

Don't rely on color alone for meaning.

Don't use more than one primary button per screen.

Don't put text smaller than 12px anywhere.

Don't skip empty states.

Don't skip loading states.

Don't hide the SOS button behind a menu.

The One-Sentence Summary
DESIGN_SYSTEM.md defines the visual language of TogetherSafe --- a calm palette (indigo primary + slate neutrals + semantic accents), two fonts (Inter + JetBrains Mono), a 4px spacing scale, consistent radii, subtle shadows, and a small set of accessible components. The SOS button is the only element allowed to use full-saturation red. If a screen uses colors, spacing, or fonts outside this document, it's wrong.