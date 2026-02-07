# Avatar Images for Rei Chatbot

This directory is where you can place your own avatar images for the chatbot interface.

## How to Add Your Own Images:

### 1. Create your images:
- **File names**: `user-avatar.png` and `bot-avatar.png` (exact names required)
- **Format**: PNG files with transparency support
- **Size**: Any size (recommended 64x64 pixels, will be auto-resized to 40x40)

### 2. Place them here:
Put your PNG files in this folder:
```
src/main/resources/images/
├── user-avatar.png    <- Your user avatar
└── bot-avatar.png     <- Your bot/chatbot avatar
```

### 3. Rebuild and run:
```bash
./gradlew build
./gradlew run --no-configuration-cache
```

## What happens:

- ✅ **If images are found**: Your custom PNG images will be used as rectangular avatars (full image visible)
- ⚠️ **If images are missing**: Attractive generated avatars will be used instead (blue gradient with person silhouette for user, green gradient with robot design for bot)

## Image Guidelines:

- Use clear, simple designs that work well in a 40x40 pixel rectangular format  
- PNG format works best (transparency supported)
- The full image will be displayed and stretched to fit the 40x40 pixel rectangle
- Images will have rounded corners for a polished look
- Good contrast against light backgrounds is recommended

## Examples of good avatar images:
- User: Profile photo, person silhouette, user icon
- Bot: Robot icon, AI symbol, chatbot mascot, "Rei" character design

**Note**: Images are displayed as rectangles with rounded corners, so the full image will be visible (no circular clipping).