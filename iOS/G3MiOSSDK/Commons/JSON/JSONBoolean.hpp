//
//  JSONBoolean.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 03/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_JSONBoolean_hpp
#define G3MiOSSDK_JSONBoolean_hpp

#include "JSONBaseObject.hpp"

class JSONBoolean : public JSONBaseObject{
private:
  bool _value;
  
public:
  JSONBoolean(bool value) :
  _value(value)
  {
  }

  const bool value() const {
    return _value;
  }
  
  JSONBoolean* asBoolean() {
    return this;
  }

  const std::string description() const;

};



#endif
