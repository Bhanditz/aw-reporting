# Change Log

All notable changes to this project will be documented in this file.

## [2.1.0] - 2017-06-08

### Changed
- Migrated to AdWords API **v201705**.

## [2.0.0] - 2017-03-06

### Added
- Automatically generated full suite of reports and fields from <code>ReportDefinitionService</code>.
- Setting <code>aw.report.downloader.excludeHiddenAccounts</code> to specify whether excluding hidden accounts.
- Setting <code>aw.report.definition.useRawEnumValues</code> to specify whether using raw enum values.

### Changed
- Migrated to AdWords API **v201702**.
- Requires Java 7 (1.7) or higher.
- Renamed database tables to match corresponding report type names with the prefix <code>AW_</code>, such as <code>AW_AccountPerformanceReport</code>.
- Renamed database column names to be the same as corresponding field names.
- Renamed processor types from <code>ON_FILE</code> and <code>ON_MEMORY</code> to <code>FILE</code> and <code>STREAM</code> respectively.
- Renamed setting <code>mccAccountId</code> to <code>managerAccountId</code> in properties file.
- Renamed setting <code>aw.report.downloader.retries.count</code> to <code>aw.report.downloader.num.attempts</code> in properties file.

### Removed
- Removed <code>aw-reporting-server</code> and <code>aw-reporting-server-appengine</code> modules due to lack of resource and popularity. If you still need them, please go to [v1.13](https://github.com/googleads/aw-reporting/tree/v1.13) branch (which will not be maintained).
- Removed <code>SQL</code> database type, which was an alias of <code>MYSQL</code>. Support remains for <code>MYSQL</code>, <code>MSSQL</code>, and <code>MONGODB</code>.
- Removed <code>-debug</code> and <code>-verbose</code> command line options. The log4j.properties file should be use to set logging granularity instead.
- Removed report writers and exporters, which are premature according to user feedback.
- Removed unnecessary setting <code>aw.report.downloader.buffer.size</code>.
