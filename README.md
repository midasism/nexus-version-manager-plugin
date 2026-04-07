# Nexus Version Manager Plugin

[![Version](https://img.shields.io/github/v/release/midasism/nexus-version-manager-plugin?style=flat-square)](https://github.com/midasism/nexus-version-manager-plugin/releases)
[![Build](https://img.shields.io/github/actions/workflow/status/midasism/nexus-version-manager-plugin/build.yml?branch=main&style=flat-square)](https://github.com/midasism/nexus-version-manager-plugin/actions)
[![License](https://img.shields.io/github/license/midasism/nexus-version-manager-plugin?style=flat-square)](LICENSE)
[![Downloads](https://img.shields.io/github/downloads/midasism/nexus-version-manager-plugin/total?style=flat-square)](https://github.com/midasism/nexus-version-manager-plugin/releases)
[![Stars](https://img.shields.io/github/stars/midasism/nexus-version-manager-plugin?style=flat-square)](https://github.com/midasism/nexus-version-manager-plugin/stargazers)

[中文](README_CN.md) | English

An IntelliJ IDEA plugin for managing Maven versions in private Nexus repositories.

## Latest Version: v1.1.1 🎉

### Core Features

- ✅ **Auto-detect API Modules**: Automatically query all Maven modules ending with `-api`
- ✅ **Version Filtering**: Switch between all/release/snapshot versions
- ✅ **Batch Display**: Show all API module versions in one query
- ✅ **Version Sorting**: Display versions in descending order, releases first
- ✅ **Quick Copy**: Double-click or right-click to copy version numbers
- ✅ **Nexus Integration**: Open corresponding Nexus page in browser
- ✅ **Config Persistence**: Auto-load saved configuration
- ✅ **Auto Refresh**: Automatically load data on first open
- ✅ **Accurate Time**: Display upload time correctly

## Compatibility

- IntelliJ IDEA 2023.1 - 2026.x
- JDK 17+
- Maven Projects

## Installation

### Method 1: Build from Source

```bash
# Clone the repository
git clone https://github.com/yourusername/nexus-version-manager-plugin.git
cd nexus-version-manager-plugin

# Build plugin
./gradlew buildPlugin
```

The plugin zip file will be generated in `build/distributions/`

### Method 2: Install from Disk

1. Go to `Settings` > `Plugins`
2. Click the gear icon ⚙️ > `Install Plugin from Disk...`
3. Select the downloaded zip file
4. Restart IDEA

## Quick Start

### 1. Configure Nexus Connection

First-time setup:

```
Settings > Tools > Nexus Version Manager
- Nexus Server URL: http://your-nexus-server.com:8081/nexus
- Auth Token: [Get NX-ANTI-CSRF-TOKEN from browser cookies]
- Connection Timeout: 30 seconds
```

**How to get Auth Token:**
1. Open Nexus in your browser and login
2. Press F12 to open DevTools
3. Go to Application > Cookies
4. Find `NX-ANTI-CSRF-TOKEN` and copy its value

### 2. Use the Plugin

1. Open a Maven project (with modules ending in `-api`)
2. Click "Nexus Versions" icon in the right toolbar
3. Data loads automatically (or click "Refresh All API Modules")
4. View version list

### 3. Version Filtering

Choose from:
- **All Versions**: Show all versions
- **Release Only**: Show only release versions
- **Snapshot Only**: Show only snapshot versions

### 4. Copy Version Numbers

- **Double-click** a version row: Copy version number
- **Right-click menu**:
  - Copy Version Number (e.g., `2.5.6`)
  - Copy Full Coordinates (e.g., `com.distinct:distinct-base-data-api:2.5.6`)
  - Open in Nexus

## Interface Preview

```
┌────────────────────────────────────────────────────┐
│ [Refresh All API Modules]                          │
│ Show: (•) All  ( ) Release  ( ) Snapshot           │
├────────────────────────────────────────────────────┤
│ Module              │Version       │Type│Upload Time│
├────────────────────────────────────────────────────┤
│distinct-base-data-api│2.6.1-SNAPSHOT│Snap│2026-01-28 │
│distinct-base-data-api│2.6.0-SNAPSHOT│Snap│2026-01-27 │
│distinct-appointment-api│3.2.1-SNAPSHOT│Snap│2026-01-28│
└────────────────────────────────────────────────────┘
Showing All Versions: 145 items (Total 145)
```

## Development

### Build Plugin

```bash
./gradlew clean buildPlugin
```

### Run in Development Mode

```bash
./gradlew runIde
```

This will start an IDEA instance with the plugin installed.

### Project Structure

```
nexus-version-manager-plugin/
├── src/main/kotlin/com/distinct/nexus/
│   ├── models/      # Data models
│   ├── services/    # Business logic
│   └── ui/          # UI components
├── src/main/resources/
│   └── META-INF/
│       └── plugin.xml
├── docs/            # Documentation
└── build.gradle.kts
```

## Documentation

- [Quick Start Guide](docs/QUICK_START.md)
- [Development Guide](docs/DEVELOPMENT.md)
- [Testing Guide](docs/TESTING.md)
- [Troubleshooting](docs/TROUBLESHOOTING.md)
- [Changelog](docs/CHANGELOG.md)
- [Upgrade Guide](docs/UPGRADE_GUIDE.md)

## Version History

### v1.1.1 (2026-04-07)
- ✅ Remove "Repository" column
- ✅ Fix upload time display
- ✅ Auto-load data on first open

### v1.1.0 (2026-04-07)
- ✅ Auto-detect all API modules
- ✅ Version filtering (All/Release/Snapshot)
- ✅ Batch query and display
- ✅ Enhanced error handling

### v1.0.0 (2026-04-07)
- Initial release

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

MIT License

## Support

- Email: gxy2825@gmail.com
- Issues: [GitHub Issues](https://github.com/midasism/nexus-version-manager-plugin/issues)
- Marketplace: Coming soon...

## Acknowledgments

Built with:
- Kotlin 1.9.21
- IntelliJ Platform SDK
- OkHttp 4.12.0
- Gson 2.10.1

---

**Make Maven version management easier!** 🚀
