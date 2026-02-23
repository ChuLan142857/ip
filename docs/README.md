# Rei User Guide

![Rei Application](screenshot.png)

**Rei** is a powerful task management application that helps you organize your todos, deadlines, and events efficiently. With features like tagging, priority management, and flexible search, Rei makes task management simple and intuitive.

---

## Table of Contents

1. [Quick Start](#quick-start)
2. [Features](#features)
3. [Command Reference](#command-reference)
   - [Adding Tasks](#adding-tasks)
   - [Managing Tasks](#managing-tasks)
   - [Searching and Filtering](#searching-and-filtering)
   - [Organization](#organization)
   - [Data Management](#data-management)
4. [FAQ](#faq)

---

## Quick Start

1. Ensure you have Java 17 or above installed on your computer.
2. Download the latest `Rei.jar` from the releases page.
3. Double-click the jar file to launch the application.
4. Type commands in the input box and press Enter to execute them.
5. Refer to the [Command Reference](#command-reference) below for details on available commands.

---

## Features

### Task Management
- **Three Task Types**: Manage todos, deadlines, and events
- **Flexible Completion**: Mark and unmark tasks as done
- **Duplicate Detection**: Automatic detection of duplicate tasks with confirmation

### Organization
- **Tags**: Add hashtags to categorize and organize tasks
- **Priority Levels**: Set HIGH, MEDIUM, or LOW priority for tasks
- **Smart Search**: Find tasks by keywords, tags, or task type
- **Sorting**: Sort tasks chronologically or by insertion order

### Advanced Features
- **Clone Tasks**: Duplicate existing tasks quickly
- **Update Tasks**: Modify task details without recreating them
- **Archive System**: Archive all tasks to start fresh with a clean slate
- **Clear Completed**: Remove only completed tasks and keep a record
- **Data Source Switching**: Switch between different data files

---

## Command Reference

### Adding Tasks

#### Adding a Todo

Add a simple task without any date/time constraints.

**Format:** `todo DESCRIPTION`

**Example:**
```
todo Buy groceries
```

**Expected Output:**
```
Got it. I've added this task:
  [T][ ] Buy groceries
Now you have 1 tasks in the list.
```

---

#### Adding a Deadline

Add a task that must be completed by a specific date and time.

**Format:** `deadline DESCRIPTION /by YYYY-MM-DD [HH:mm]`

**Examples:**
```
deadline Submit report /by 2026-03-15 23:59
deadline Pay bills /by 2026-02-28
```

**Expected Output:**
```
Got it. I've added this task:
  [D][ ] Submit report (by: Mar 15 2026 23:59)
Now you have 2 tasks in the list.
```

**Notes:**
- Time is optional; if omitted, defaults to 23:59
- Date format must be YYYY-MM-DD

---

#### Adding an Event

Add a task that occurs during a specific time period.

**Format:** `event DESCRIPTION /from YYYY-MM-DD [HH:mm] /to YYYY-MM-DD [HH:mm]`

**Example:**
```
event Team meeting /from 2026-03-01 14:00 /to 2026-03-01 16:00
```

**Expected Output:**
```
Got it. I've added this task:
  [E][ ] Team meeting (from: Mar 1 2026 14:00 to: Mar 1 2026 16:00)
Now you have 3 tasks in the list.
```

---

### Managing Tasks

#### Listing All Tasks

Display all tasks in your list.

**Format:** `list`

**Expected Output:**
```
Here are the tasks in your list:
1.[T][ ] Buy groceries
2.[D][ ] Submit report (by: Mar 15 2026 23:59)
3.[E][ ] Team meeting (from: Mar 1 2026 14:00 to: Mar 1 2026 16:00)
```

---

#### Marking a Task as Done

Mark a task as completed.

**Format:** `mark TASK_NUMBER`

**Example:**
```
mark 1
```

**Expected Output:**
```
Nice! I've marked this task as done:
  [T][X] Buy groceries
```

---

#### Unmarking a Task

Mark a completed task as not done.

**Format:** `unmark TASK_NUMBER`

**Example:**
```
unmark 1
```

**Expected Output:**
```
OK, I've marked this task as not done yet:
  [T][ ] Buy groceries
```

---

#### Deleting a Task

Remove a task from your list permanently.

**Format:** `delete TASK_NUMBER`

**Example:**
```
delete 2
```

**Expected Output:**
```
Noted. I've removed this task:
  [D][ ] Submit report (by: Mar 15 2026 23:59)
Now you have 2 tasks in the list.
```

---

### Searching and Filtering

#### Finding Tasks

Search for tasks by keywords. Supports multiple keywords and searches across descriptions, task types, and tags.

**Format:** `find KEYWORD [MORE_KEYWORDS...]`

**Examples:**
```
find meeting
find report deadline
```

**Expected Output:**
```
Here are the matching tasks in your list (OR search):
1.[E][ ] Team meeting (from: Mar 1 2026 14:00 to: Mar 1 2026 16:00)
```

**Notes:**
- Search is case-insensitive
- Multiple keywords use OR logic by default (matches any keyword)
- Partial matches are supported

---

### Organization

#### Adding Tags

Add hashtags to categorize tasks.

**Format:** `tag TASK_NUMBER #TAGNAME`

**Example:**
```
tag 1 #personal
```

**Expected Output:**
```
Got it. I've added the tag to this task:
  [T][ ] Buy groceries #personal
```

---

#### Removing Tags

Remove a specific tag from a task.

**Format:** `untag TASK_NUMBER #TAGNAME`

**Example:**
```
untag 1 #personal
```

**Expected Output:**
```
Got it. I've removed the tag from this task:
  [T][ ] Buy groceries
```

---

#### Setting Priority

Set a priority level for a task.

**Format:** `priority TASK_NUMBER LEVEL`

**Priority Levels:** `HIGH` (or `1`), `MEDIUM` (or `2`), `LOW` (or `3`)

**Examples:**
```
priority 1 HIGH
priority 2 1
priority 3 MEDIUM
```

**Expected Output:**
```
Got it. I've set the priority for this task:
  [T][ ][HIGH] Buy groceries
```

---

#### Sorting Tasks

Sort your task list in different ways.

**Format:** 
- `sort` - Sort chronologically (deadlines/events by date, todos at end)
- `sort insertion` - Sort by original insertion order

**Example:**
```
sort
```

**Expected Output:**
```
Tasks sorted chronologically.
Deadlines and events are sorted by date, todos are at the end.
```

---

#### Cloning a Task

Create a duplicate of an existing task.

**Format:** `clone TASK_NUMBER`

**Example:**
```
clone 1
```

**Expected Output:**
```
Got it. I've cloned this task:
  [T][ ] Buy groceries
Now you have 4 tasks in the list.
```

---

#### Updating a Task

Modify task details without recreating the task.

**Format:**
- `update TASK_NUMBER /d NEW_DESCRIPTION` - Update description
- `update TASK_NUMBER /by YYYY-MM-DD [HH:mm]` - Update deadline
- `update TASK_NUMBER /from YYYY-MM-DD [HH:mm] /to YYYY-MM-DD [HH:mm]` - Update event times

**Examples:**
```
update 1 /d Buy groceries and cook dinner
update 2 /by 2026-03-20 18:00
update 3 /from 2026-03-02 10:00 /to 2026-03-02 12:00
```

**Expected Output:**
```
Got it. I've updated this task:
  [T][ ] Buy groceries and cook dinner
```

---

### Data Management

#### Archiving All Tasks

Archive all tasks to a timestamped file and start with a clean list.

**Format:** `archive`

**Expected Output:**
```
Archived 5 task(s) to:
data/Rei_archive_20260223_143025.txt
Your task list has been cleared. You may now start with a fresh slate.
Or perhaps it's time to stop for a coffee break?
```

**Notes:**
- Archives ALL tasks (completed and incomplete)
- Creates a timestamped file: `Rei_archive_YYYYMMDD_HHMMSS.txt`
- Completely clears your active task list

---

#### Clearing Completed Tasks

Remove only completed tasks and record them.

**Format:** `clear`

**Expected Output:**
```
Cleared 3 completed task(s) and recorded in:
data/Rei_completed.txt
Your active task list has been updated. Keep on fighting.
```

**Notes:**
- Only removes completed tasks
- Appends completed tasks to `Rei_completed.txt` for permanent record
- Active (incomplete) tasks remain in your list

---

#### Checking Data Source

View the current data file being used.

**Format:** `source`

**Expected Output:**
```
Current data source: data/Rei.txt
```

---

#### Switching Data Source

Switch to a different data file.

**Format:** `source FILE_PATH`

**Example:**
```
source data/work_tasks.txt
```

**Expected Output:**
```
Switching data source to: data/work_tasks.txt
Don't worry, your current tasks will be saved before switching.
```

**Notes:**
- If the file doesn't exist, Rei will ask for confirmation to create it
- Current tasks are saved before switching

---

#### Exiting the Application

Close Rei and save your tasks.

**Format:** `bye`

**Expected Output:**
```
Bye. Hope to see you again soon!
```

---

## FAQ

**Q: How do I know what task number to use for commands?**  
A: Use the `list` command to see all your tasks with their numbers.

**Q: Can I add multiple tags to a task?**  
A: Yes! Use the `tag` command multiple times with different tags.

**Q: What's the difference between archive and clear?**  
A: `archive` saves ALL tasks (completed and incomplete) to a timestamped file and clears your entire list. `clear` only removes completed tasks and appends them to a persistent record file.

**Q: Can I search for tasks by tags?**  
A: Yes! The `find` command searches across descriptions, task types, and tags.

**Q: What happens if I try to add a duplicate task?**  
A: Rei will detect the duplicate and ask for confirmation before adding it.

**Q: Are my tasks saved automatically?**  
A: Yes! Tasks are automatically saved after every command that modifies your task list.

**Q: Can I undo a command?**  
A: Currently, there's no undo feature. However, archived and completed tasks are preserved in separate files.

---

**Need more help?** Check the command reference above or type commands to see what happens!