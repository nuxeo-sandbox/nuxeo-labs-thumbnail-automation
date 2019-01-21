# nuxeo-labs-thumbnail-automation

The plug-in allows for calculating a thumbnail using automation.

Basically:

1. Contribute an extension point telling the service which automation chain to use
2. Override one (or more) of the default ThumbnailFactory
3. Implement the chain. It receives the `Document` as input and must return a `Blob` as output.

## Configuration

### 1. Tell the Service Which Automation Chain to Use
```
<extension target="nuxeo.labs.thumbnail.automation" point="configuration">
  <configuration>
    <chainId>HERE_YOUR_CHAIN_ID</chainId>
  </configuration>
</extension>
```

### 2. Override one (or more) of the default ThumbnailFactory

This is done by telling the ThumbnailServoce to use the specific factory class of this plugin, available at `nuxeo.labs.thumbnail.automation.ThumbnailAutomationFactory`.

Here, we override the Picture thumbnail factory:

```
<extension
    target="org.nuxeo.ecm.core.api.thumbnail.ThumbnailService"
    point="thumbnailFactory">
    
  <thumbnailFactory
      name="thumbnailPictureFactory"
      facet="Picture"
      factoryClass="nuxeo.labs.thumbnail.automation.ThumbnailAutomationFactory" />
    
</extension>
```

### 3. Implement the Chain

In this example, the `picture:views` schema has the default renditions plus one, named "SmallWM", that is a watermarked rendition. We return the default, "Small" rendition or the "SmallWM" one depending on the current user. We use JavaScript automation:

```
/* Picture_getThumbnail
   Called by the the nuxeo-labs-thumbnali-automation plugin
*/
function run(input, params) {
  
  var thumbnail;
    
  if(currentUser.name === "Administrator") {
    thumbnail = Picture.GetView(input, {'viewName': "Small"});
  } else {
    thumbnail = Picture.GetView(input, {'viewName': "SmallWM"});
  }
  
  return thumbnail;
}
```


This chain was contributed as:

```
**<extension target="nuxeo.labs.thumbnail.automation" point="configuration">
  <configuration>
    <chainId>javascript.Picture_getThumbnail</chainId>
  </configuration>
</extension>**
```

## WARNING

The plugin is not unit tested.

Also we did not test scalability and speed. For speed, it would be more efficient to build a custom Java plugin implementing the logic, instead of calling automation. The plug-in is convenient for quick demos.

## Build

    git clone https://github.com/nuxeo-sandbox/nuxeo-labs-thumbnail-automation.git
    cd nuxeo-labs-thumbnail-automation
    
    mvn clean install


## Support

**These features are not part of the Nuxeo Production platform, they are not supported**

These solutions are provided for inspiration and we encourage customers to use them as code samples and learning resources.

This is a moving project (no API maintenance, no deprecation process, etc.) If any of these solutions are found to be useful for the Nuxeo Platform in general, they will be integrated directly into platform, not maintained here.


# Licensing

[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)


# About Nuxeo

Nuxeo dramatically improves how content-based applications are built, managed and deployed, making customers more agile, innovative and successful. Nuxeo provides a next generation, enterprise ready platform for building traditional and cutting-edge content oriented applications. Combining a powerful application development environment with SaaS-based tools and a modular architecture, the Nuxeo Platform and Products provide clear business value to some of the most recognizable brands including Verizon, Electronic Arts, Sharp, FICO, the U.S. Navy, and Boeing. Nuxeo is headquartered in New York and Paris.

More information is available at [www.nuxeo.com](http://www.nuxeo.com). 
