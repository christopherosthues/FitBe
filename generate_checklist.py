from pathlib import Path

def create_markdown_checklist(root_dir, output_file):
    """
    Generates a markdown file with a checklist of all Kotlin files
    in the specified directory, grouped by package.
    """
    root_path = Path(root_dir)
    output_path = Path(output_file)

    # Using a defaultdict to store the file paths under their package directories.
    files_by_package = dict()

    # Find all .kt files and group them by their parent directory.
    for path in sorted(root_path.rglob("*.kt")):
        # The package path is the file's parent directory.
        package_path = path.parent
        if package_path not in files_by_package:
            files_by_package[package_path] = list()
        files_by_package[package_path].append(path.stem)

    with output_path.open("w", encoding="utf-8") as f:
        # We'll keep track of the last written path to manage headers correctly.
        last_parts = []
        # Sort the packages alphabetically to ensure a consistent output.
        for package_path in sorted(files_by_package.keys(), key=lambda p: p.parts):
            parts = package_path.relative_to(root_path).parts

            # Figure out the common path between the current and last package.
            common_depth = 0
            for i in range(min(len(last_parts), len(parts))):
                if last_parts[i] != parts[i]:
                    break
                common_depth += 1

            # Add headers for the new package parts.
            for i in range(common_depth, len(parts)):
                f.write(f"{'#' * (i + 1)} {parts[i]}\n")

            f.write("\n")

            # Write the checklist for the files in the current package.
            for file_name in sorted(files_by_package[package_path]):
                f.write(f"* [ ] {file_name}\n")
            f.write("\n")

            last_parts = parts

if __name__ == "__main__":
    # The starting directory for the search.
    start_directory = "D:/home/git/FitBe/composeApp/src/commonMain/kotlin"
    # The file where the output will be saved.
    output_markdown_file = "D:/home/git/FitBe/Refactor.md"
    create_markdown_checklist(start_directory, output_markdown_file)