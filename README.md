# Kemitix Trello

Wrapper for Trello.

![GitHub release (latest by date)](
https://img.shields.io/github/v/release/kemitix/kemitix-trello?style=for-the-badge)
![GitHub Release Date](
https://img.shields.io/github/release-date/kemitix/kemitix-trello?style=for-the-badge)

[![Nexus](
https://img.shields.io/nexus/r/https/oss.sonatype.org/net.kemitix/kemitix-trello.svg?style=for-the-badge)](
https://oss.sonatype.org/content/repositories/releases/net/kemitix/kemitix-trello/)
[![Maven-Central](
https://img.shields.io/maven-central/v/net.kemitix/kemitix-trello.svg?style=for-the-badge)](
https://search.maven.org/search?q=g:net.kemitix%20a:kemitix-trello)

This wraps the `com.taskadapter:trello-java-wrapper` library.

It is intended for use on my `slushy` and `fuller` projects. It was originally developed as part of `slushy` and was extracted when preparing to start on `fuller` which would need some of the same functionality.

Requires JDK 11+

## Downloading Attachments

From 1.1.0 of `kemitix-trello` support is included for downloading attachments
using the now required authorised methods. In order to do so the API key and 
token are needed to download the attachment. These are provided either when 
creating the new `TrelloAttachment` constructor, or by calling the method
`withApiKeyPair(...)`, which will return a new instance.
